package com.harukadev.linko.api

import com.harukadev.linko.data.ApiResponse
import com.harukadev.linko.data.ShortenerException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class Shortener {
    private val ktorClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun shortenUrl(url: String): String {
        return try {
            val response = callApi(url)
            processResult(response)
        } finally {
            closeKtorClient()
        }
    }


    private suspend fun callApi(url: String): HttpResponse {
        val response = ktorClient.get("https://is.gd/create.php?format=json&url=$url")
        return response
    }

    private suspend fun processResult(response: HttpResponse): String {
        val result = Json.decodeFromString<ApiResponse>(response.body<String>())

        return when (result.errorCode) {
            1 -> "there was a problem with the original long URL provided" + result.errorMessage
            2 -> "there was a problem with the short URL provided (for custom short URLs)"
            3 -> "our rate limit was exceeded (you should wait before trying again)"
            4 -> "any other error (includes potential problems with our service such as a maintenance period)"
            else -> result.shortUrl!!
        }
    }

    private fun closeKtorClient() {
        runBlocking {
            ktorClient.close()
        }
    }
}

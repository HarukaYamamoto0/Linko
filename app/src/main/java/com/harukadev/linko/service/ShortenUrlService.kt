package com.harukadev.linko.service

import com.harukadev.linko.data.ResponseSuccess
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ShortenURL {
    private val baseUrl = "https://spoo.me/"

    private val ktorClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging)
    }

    suspend fun shorten(url: String, surname: String?): Result<String> {
        return runCatching {
            val response = ktorClient.post(baseUrl) {
                contentType(ContentType.Application.FormUrlEncoded)
                headers {
                    append(HttpHeaders.Accept, "application/json")
                }
                setBody("url=$url" + if (surname != null) "&alias=$surname" else "")
            }

            return if (response.status.value == 200) {
                Result.success(response.body<ResponseSuccess>().url)
            } else {
                Result.failure(Exception(response.body<String>()))
            }
        }
    }

    fun getUrlForStatistics(url: String): String {
        return baseUrl + "stats/" + url.split("/").last()
    }

    fun getErrorMessage(error: String): String {
        val regex = """\{\s*"(.*?)"\s*:\s*"(.*?)"\s*,?""".toRegex()
        val matchResult = regex.find(error)

        return if (matchResult != null) {
            val (_, value) = matchResult.destructured
            value.lowercase()
        } else "unknown error"
    }


    fun closeClient() {
        ktorClient.close()
    }
}
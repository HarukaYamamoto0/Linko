package com.harukadev.linko.api

import com.harukadev.linko.data.ShortenedUrl
import com.harukadev.linko.utils.Promise
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class Shortener {
    private val ktorClient: HttpClient by lazy {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    suspend fun shortenUrl(url: String, surname: String? = null): Promise<ShortenedUrl> {
        return try {
            val response: ShortenedUrl = ktorClient.post(BASE_URL) {
                contentType(ContentType.Application.FormUrlEncoded)
                headers {
                    append(HttpHeaders.Accept, "application/json")
                }
                setBody("url=$url&surname=${surname ?: ""}")
            }.body()

            Promise.Success(response)
        } catch (e: Exception) {
            Promise.Error(e)
        }
    }

    fun closeClient() {
        ktorClient.close()
    }

    companion object {
        const val BASE_URL = "https://spoo.me/"
        const val BASE_URL_FOR_STATISTICS = "https://spoo.me/stats/"
    }
}

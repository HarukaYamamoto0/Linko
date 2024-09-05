package com.harukadev.linko.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    @SerialName("shorturl") val shortUrl: String? = null,
    @SerialName("errorcode") val errorCode: Int = 0,
    @SerialName("errormessage") val errorMessage: String? = null
)

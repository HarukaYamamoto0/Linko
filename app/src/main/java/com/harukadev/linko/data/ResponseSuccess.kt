package com.harukadev.linko.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseSuccess(
    @SerialName("short_url")
    val url: String
)
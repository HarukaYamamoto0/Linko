package com.harukadev.linko.data

data class ShortenerException(
    val errorCode: Int,
    val errorMessage: String
) : Error(errorMessage)
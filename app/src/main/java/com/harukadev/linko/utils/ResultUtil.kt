package com.harukadev.linko.utils

sealed class ResultUtil<out R> {
    data class Success<out T>(val data: T) : ResultUtil<T>()
    data class Error(val exception: Exception) : ResultUtil<Nothing>()
}

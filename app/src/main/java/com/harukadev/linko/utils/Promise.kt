package com.harukadev.linko.utils

sealed class Promise<out R> {
    data class Success<T>(val data: T) : Promise<T>()
    data class Error(val exception: Exception) : Promise<Nothing>()
    data object Loading : Promise<Nothing>()

    fun isSuccess(): Boolean = this is Success

    fun isError(): Boolean = this is Error

    fun isLoading(): Boolean = this is Loading
}

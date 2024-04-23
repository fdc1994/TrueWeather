package com.example.trueweather.utils

sealed class RxResult<out T> {
    data class Success<out T>(val data: T) : RxResult<T>()
    data class Error(val errorType: ErrorType) : RxResult<Nothing>()

    fun getValueOrNull(): T? {
        return when (this) {
            is Success -> data
            is Error -> null
        }
    }
}

enum class ErrorType {
    NETWORK_ERROR,
    TIMEOUT_ERROR,
    GENERIC_ERROR
}

package com.example.domain.data.utils

sealed class ResultWrapper<out T> {
    object Loading : ResultWrapper<Nothing>()
    data class Success<out T>(val data: T) : ResultWrapper<T>()
    data class Error(val errorType: ErrorType) : ResultWrapper<Nothing>()
}

enum class ErrorType {
    NETWORK_ERROR,
    TIMEOUT_ERROR,
    GENERIC_ERROR
}

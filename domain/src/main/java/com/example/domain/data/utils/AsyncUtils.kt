package com.example.domain.data.utils

sealed class ResultWrapper<out T> {
    data object Loading : ResultWrapper<Nothing>()
    data class Success<out T>(val data: T) : ResultWrapper<T>()
    data class Error(val errorType: ErrorType) : ResultWrapper<Nothing>()
}

enum class ErrorType {
    NETWORK_ERROR,
    GENERIC_ERROR
}

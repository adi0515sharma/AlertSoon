package com.example.notifyme.ui.utils

sealed class ApiResponse<T> {
    data class Success<T>(val data: T?) : ApiResponse<T>()
    data class Error<T>(val message: String?) : ApiResponse<T>()
    data class Loading<T>(val data : Any?) : ApiResponse<T>()

}

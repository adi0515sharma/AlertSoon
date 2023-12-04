package com.example.notifyme.ui.utils


sealed class ValidatorResponse {
    object Success : ValidatorResponse()
    data class Error(val message: String) : ValidatorResponse()
}

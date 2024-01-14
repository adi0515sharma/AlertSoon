package com.alertSoon.alarm.ui.utils


sealed class ValidatorResponse {
    object Success : ValidatorResponse()
    data class Error(val message: String) : ValidatorResponse()
}

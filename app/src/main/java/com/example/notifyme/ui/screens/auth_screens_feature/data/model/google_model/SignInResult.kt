package com.example.notifyme.ui.screens.auth_screens_feature.data.model.google_model

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
)
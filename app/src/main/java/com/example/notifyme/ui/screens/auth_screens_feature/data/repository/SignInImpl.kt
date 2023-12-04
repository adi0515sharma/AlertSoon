package com.example.notifyme.ui.screens.auth_screens_feature.data.repository

import com.example.notifyme.ui.screens.auth_screens_feature.data.api.GoogleAuthUiClient
import com.example.notifyme.ui.screens.auth_screens_feature.domain.repository.SignInImplementation
import javax.inject.Inject

class SignInImpl @Inject constructor(
    val googleAuthUiClient: GoogleAuthUiClient
) : SignInImplementation {
    override fun SignInWithGoogle() {
    }

    override fun SignInWithFacebook() {

    }

    override fun SignInWithEmailIdAndPassword(email: String, password: String) {

    }
}
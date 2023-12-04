package com.example.notifyme.ui.screens.auth_screens_feature.domain.repository

interface SignInImplementation {

    public fun SignInWithGoogle()
    public fun SignInWithFacebook()
    public fun SignInWithEmailIdAndPassword(email : String, password : String)
}
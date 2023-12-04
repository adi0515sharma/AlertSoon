package com.example.notifyme.ui.di

import android.content.Context
import com.example.notifyme.ui.screens.auth_screens_feature.data.api.BasicFirebaseAuth
import com.example.notifyme.ui.screens.auth_screens_feature.data.api.FacebookAuthUiClient
import com.example.notifyme.ui.screens.auth_screens_feature.data.api.GoogleAuthUiClient
import com.google.android.gms.auth.api.identity.Identity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class AuthenticationDI {

    @Provides
    fun getGoogleAuthUiClient(@ApplicationContext context: Context) : GoogleAuthUiClient {
        return GoogleAuthUiClient(context, Identity.getSignInClient(context))
    }



    @Provides
    fun getBasicFirebaseAuth() : BasicFirebaseAuth {
        return BasicFirebaseAuth()
    }
}
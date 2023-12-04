package com.example.notifyme.ui.screens.auth_screens_feature.data.api

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import com.example.notifyme.ui.screens.auth_screens_feature.data.model.google_model.SignInResult
import com.example.notifyme.ui.screens.auth_screens_feature.data.model.google_model.UserData
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FacebookAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FacebookAuthUiClient @Inject constructor(
    private val context: Context,
    private val callbackManager: CallbackManager
) : BasicFirebaseAuth() {

    fun signInWithFacebook(facebookLoginStatus: FacebookLoginStatus){
        val loginManager = LoginManager.getInstance()
        loginManager.logInWithReadPermissions(
            context as Activity,
            listOf("public_profile")
        )

        // Callback registration
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                // Handle Facebook login success
                result?.let {
                    val token = it.accessToken
                    runBlocking(Dispatchers.IO){
                        val signInResult = handleFacebookAccessToken(token)
                        if(signInResult.data!=null){
                            facebookLoginStatus.onSuceess(signInResult = signInResult.data)

                        }
                        else{
                            facebookLoginStatus.onError(message = signInResult.errorMessage?:"Something went wrong")
                        }
                    }
                }
            }

            override fun onCancel() {
                // Handle Facebook login cancellation
            }

            override fun onError(error: FacebookException) {
                // Handle Facebook login error
                facebookLoginStatus.onError(message = error.message)
            }
        })
    }

    private suspend fun handleFacebookAccessToken(token: AccessToken) : SignInResult {


        val credential = FacebookAuthProvider.getCredential(token.token)

        return try {
            val user = auth.signInWithCredential(credential).await().user
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        } catch(e: Exception) {
            e.printStackTrace()
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }

    }
}

interface FacebookLoginStatus{
    public fun onSuceess(signInResult: UserData)
    public fun onError(message : String? = null)
}
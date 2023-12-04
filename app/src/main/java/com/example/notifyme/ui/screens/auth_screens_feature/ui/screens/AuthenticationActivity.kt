package com.example.notifyme.ui.screens.auth_screens_feature.ui.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notifyme.ui.utils.ApiResponse
import com.example.notifyme.ui.screens.auth_screens_feature.data.api.BasicFirebaseAuth
import com.example.notifyme.ui.screens.auth_screens_feature.data.api.FacebookAuthUiClient
import com.example.notifyme.ui.screens.auth_screens_feature.data.api.FacebookLoginStatus
import com.example.notifyme.ui.screens.auth_screens_feature.data.api.GoogleAuthUiClient
import com.example.notifyme.ui.screens.auth_screens_feature.data.model.google_model.UserData
import com.example.notifyme.ui.navigation.AuthenticationNavHost
import com.example.notifyme.ui.screens.home_screen_activity.ui.HomeActivity
import com.example.notifyme.ui.theme.NotifyMeTheme
import com.facebook.CallbackManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticationActivity : ComponentActivity() {


    private val callbackManager = CallbackManager.Factory.create()

    private lateinit var authenticationViewmodel: AuthenticationViewmodel


    @Inject
    lateinit var googleAuthUiClient : GoogleAuthUiClient

    @Inject
    lateinit var basicFirebaseAuth: BasicFirebaseAuth


    lateinit var facebookAuthUiClient: FacebookAuthUiClient


    override fun onStart() {
        super.onStart()
        if(googleAuthUiClient.getSignedInUser()!=null){
            startActivity(Intent(this@AuthenticationActivity, HomeActivity::class.java))
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        facebookAuthUiClient = FacebookAuthUiClient(this@AuthenticationActivity, callbackManager)

        setContent {
            NotifyMeTheme {

                Surface() {
                    var googleIntent by rememberSaveable { mutableStateOf<Intent?>(null) }


                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                        onResult = { result ->
                            if(result.resultCode == ComponentActivity.RESULT_OK) {

                                lifecycleScope.launch {
                                    googleIntent = result.data

                                    if(googleIntent==null)
                                        return@launch

                                    val signInResult = googleAuthUiClient.signInWithIntent(
                                        intent = googleIntent!!
                                    )
                                    if(signInResult.errorMessage!=null){

                                        authenticationViewmodel.setNewSignIn(ApiResponse.Error<UserData>(signInResult.errorMessage))

                                    }
                                    else{
                                        authenticationViewmodel.setNewSignIn(ApiResponse.Success<UserData>(signInResult.data!!))
                                    }

                                }

                            }
                            else{

                            }
                        }
                    )
                    authenticationViewmodel = viewModel<AuthenticationViewmodel>()

                    LaunchedEffect(key1 = null){
                        authenticationViewmodel.signIn.collectLatest {
                            if(it is ApiResponse.Success) {
                                startActivity(
                                    Intent(
                                        this@AuthenticationActivity,
                                        HomeActivity::class.java
                                    )
                                )
                                finish()
                            }
                            else if(it is ApiResponse.Error){
                            }
                        }
                    }

                    AuthenticationNavHost(

                        onGoogleSignIn = {
                            lifecycleScope.launch {
                                val signInIntentSender = googleAuthUiClient.signIn()
                                launcher.launch(
                                    IntentSenderRequest.Builder(
                                        signInIntentSender ?: return@launch
                                    ).build()
                                )
                            }
                        },

                        onFacebookSignIn = {
                            lifecycleScope.launch {
                                facebookAuthUiClient.signInWithFacebook(
                                    object : FacebookLoginStatus{
                                        override fun onSuceess(userData: UserData) {
                                            authenticationViewmodel.setNewSignIn(ApiResponse.Success<UserData>(userData))

                                        }

                                        override fun onError(message: String?) {
                                            authenticationViewmodel.setNewSignIn(ApiResponse.Error<UserData>(message!!))

                                        }
                                    }
                                )
                            }
                        },
                        onNormalSignIn = { email: String, password: String ->
                            lifecycleScope.launch {
                                val signInResult =basicFirebaseAuth.signInWithEmailAndPassword(email, password)
                                if(signInResult.errorMessage!=null){

                                    authenticationViewmodel.setNewSignIn(ApiResponse.Error<UserData>(signInResult.errorMessage))

                                }
                                else{
                                    authenticationViewmodel.setNewSignIn(ApiResponse.Success<UserData>(signInResult.data!!))
                                }
                            }
                        },

                        authenticationViewmodel,
                        basicFirebaseAuth
                    )
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}
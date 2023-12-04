package com.example.notifyme.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notifyme.ui.screens.auth_screens_feature.data.api.BasicFirebaseAuth
import com.example.notifyme.ui.screens.auth_screens_feature.ui.screens.AuthenticationViewmodel
import com.example.notifyme.ui.screens.auth_screens_feature.ui.screens.sign_in_screen_component.SignInComposableScreen
import com.example.notifyme.ui.screens.auth_screens_feature.ui.screens.sign_up_screen_component.SignUpComposableScreen

@Composable
fun AuthenticationNavHost(onGoogleSignIn: () -> Unit, onFacebookSignIn: () -> Unit, onNormalSignIn: (email : String, password : String) -> Unit, authenticationViewmodel: AuthenticationViewmodel, basicFirebaseAuth: BasicFirebaseAuth) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = AuthNavScreen.SIGN_IN.name) {

        composable(route = AuthNavScreen.SIGN_IN.name) {
            SignInComposableScreen(navController, onGoogleSignIn, onFacebookSignIn,onNormalSignIn)
        }
        composable(route = AuthNavScreen.SIGN_UP.name) {
            SignUpComposableScreen(navController = navController, basicFirebaseAuth = basicFirebaseAuth, authenticationViewmodel = authenticationViewmodel)
        }
    }
}

sealed class AuthNavScreen(val name : String){
    object SIGN_IN : AuthNavScreen("SIGN_IN")
    object SIGN_UP : AuthNavScreen("SIGN_UP")
}
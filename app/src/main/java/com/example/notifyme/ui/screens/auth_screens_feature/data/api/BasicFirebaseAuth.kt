package com.example.notifyme.ui.screens.auth_screens_feature.data.api

import android.util.Log
import com.example.notifyme.ui.screens.auth_screens_feature.data.model.google_model.SignInResult
import com.example.notifyme.ui.screens.auth_screens_feature.data.model.google_model.UserData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

open class BasicFirebaseAuth {

    val auth = FirebaseAuth.getInstance()
    suspend fun signOut() {
        try {
            auth.signOut()
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        Log.e("CurrentUser", "id = ${this.uid}")
        Log.e("CurrentUser", "name = ${this.displayName}")
        Log.e("CurrentUser", "email = ${this.email}")

        UserData(

            userId = email?:uid,
            username = displayName,
            profilePictureUrl = photoUrl?.toString()
        )
    }

    public suspend fun signInWithEmailAndPassword(email: String, password : String) : SignInResult{
        return try{
            val firebaseUser = auth.signInWithEmailAndPassword(email, password).await().user
            SignInResult(
                data = firebaseUser?.run {
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


    public suspend fun signUpWithEmailAndPassword(email: String, password : String) : SignInResult{
        return try{
            val firebaseUser = auth.createUserWithEmailAndPassword(email, password).await().user
            SignInResult(
                data = firebaseUser?.run {
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
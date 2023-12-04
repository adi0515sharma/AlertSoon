package com.example.notifyme.ui.screens.auth_screens_feature.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notifyme.ui.utils.ApiResponse
import com.example.notifyme.ui.screens.auth_screens_feature.data.model.google_model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewmodel @Inject constructor(): ViewModel() {

    private var _signIn : MutableSharedFlow<ApiResponse<UserData>> = MutableSharedFlow<ApiResponse<UserData>>()
    val signIn : SharedFlow<ApiResponse<UserData>> = _signIn

    public fun setNewSignIn(user : ApiResponse<UserData>){
        viewModelScope.launch {
            _signIn.emit(user)
        }
    }

}
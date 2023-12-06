package com.example.AlertSoon.ui.utils

import android.util.Patterns

object Validator {

    public fun String.isEmailValid() : ValidatorResponse{
        if(this.isNullOrEmpty()){
            return ValidatorResponse.Error("Please provide email id")
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(this).matches()){
            return ValidatorResponse.Error("Please provide proper email id")
        }

        return ValidatorResponse.Success
    }

    fun String?.isFieldCorrect(about : String) : ValidatorResponse{
        if(this.isNullOrEmpty()){
            return ValidatorResponse.Error("Please provide ${about}")
        }
        return ValidatorResponse.Success
    }

    fun String.isDayFieldCorrect() : ValidatorResponse{
        if(!this.contains("1")){
            return ValidatorResponse.Error("Please provide days for alarm")
        }
        return ValidatorResponse.Success
    }
}
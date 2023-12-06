package com.example.AlertSoon.ui.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import javax.inject.Inject


class AlertSoonSharePref @Inject constructor(context: Context){

    private val sharedPreferences = context.getSharedPreferences("AlertSoonSharePref", Context.MODE_PRIVATE);

    companion object{
        const val otherSetting = "otherSetting"
        const val lastOs = "lastOs"
    }



    fun saveOtherSettingValue(value : Boolean){
        var editor: SharedPreferences.Editor = sharedPreferences.edit();
        editor.putBoolean(otherSetting, value);
        editor.apply();
    }

    fun getOtherSettingValue() : Boolean{
        return sharedPreferences.getBoolean(otherSetting,false)
    }

    fun saveLastOs(){
        var editor: SharedPreferences.Editor = sharedPreferences.edit();
        editor.putString(lastOs, Build.VERSION.RELEASE);
        editor.apply();
    }

    fun getLastOs() : String?{
        return sharedPreferences.getString(lastOs,null)
    }

}
package com.example.notifyme.ui.utils

import android.content.Context
import android.os.PowerManager

abstract class WakeLocker {

    companion object{

        var wakeLock : PowerManager.WakeLock? = null

        fun acquire(context : Context?){
            if(isDeviceScreenOn(context)){
                return
            }
            val powerManager : PowerManager = context!!.getSystemService(Context.POWER_SERVICE) as PowerManager
            wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE, ":NotifyMe")
            wakeLock?.setReferenceCounted(false);
            wakeLock?.acquire(10 * 60 * 1000L)
        }

        fun release(){
            if(wakeLock!=null)
                wakeLock?.release()
            wakeLock = null
        }

        private fun isDeviceScreenOn(context: Context?): Boolean {
            val powerManager = context?.getSystemService(Context.POWER_SERVICE) as PowerManager
            return powerManager.isInteractive
        }

    }
}
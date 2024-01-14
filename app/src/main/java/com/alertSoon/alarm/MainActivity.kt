package com.alertSoon.alarm

import  android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class MainActivity : Application() {


    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
            createNotificationChannelForRescedulingNotification()
        }





    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channelId = "notification_id_high"
        val channelName = "For showing notification"
        val channelDescription = "For showing notification"

        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        channel.description = channelDescription

        channel.lightColor = Color.Red.hashCode()
        channel.setSound(null, null)
        channel.enableVibration(true)
        channel.lockscreenVisibility =  Notification.VISIBILITY_PUBLIC
        channel.vibrationPattern = longArrayOf(
            1000,
            2000,
            1000,
            2000,
            1000,
            2000,
            1000,
            2000,
            1000,
            2000,
            1000,
            2000,
            1000,
            2000,
            1000,
            2000,
            1000,
            2000,
            1000,
            2000,
            1000,
            2000,
            1000,
            2000
        )
        channel.enableLights(true)


        val notificationManager = NotificationManagerCompat.from(this)
        if (notificationManager.getNotificationChannel(channelId) == null) {
            notificationManager.createNotificationChannel(channel)
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannelForRescedulingNotification() {
        val channelId = "notification_again"
        val channelName = "For showing notification"
        val channelDescription = "For showing notification"

        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        channel.description = channelDescription


        val notificationManager = NotificationManagerCompat.from(this)
        if (notificationManager.getNotificationChannel(channelId) == null) {
            notificationManager.createNotificationChannel(channel)
        }

    }
}

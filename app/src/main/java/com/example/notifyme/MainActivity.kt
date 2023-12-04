package com.example.notifyme

import android.app.AlarmManager
import  android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.core.app.NotificationManagerCompat
import com.example.notifyme.ui.local_storage.Task.TableOfTask
import com.example.notifyme.ui.utils.NotifyMeSharePref
import com.example.notifyme.ui.utils.notification.NotificationReceiver
import com.facebook.FacebookSdk
import dagger.hilt.android.HiltAndroidApp
import java.util.Calendar
import javax.inject.Inject


@HiltAndroidApp
class MainActivity : Application() {

    @Inject lateinit var notifyMeSharePref: NotifyMeSharePref

    override fun onCreate() {
        super.onCreate()
        FacebookSdk.sdkInitialize(applicationContext);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        if(notifyMeSharePref.getLastOs() == null || notifyMeSharePref.getLastOs() != Build.VERSION.RELEASE){
            notifyMeSharePref.saveLastOs()
            notifyMeSharePref.saveOtherSettingValue(false)
        }



//        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//
//        var calendarTime = Calendar.getInstance()
//        calendarTime.set(Calendar.SECOND, 0)
//        calendarTime.add(Calendar.MINUTE, 10)
//
//        val tableOfTask = TableOfTask(
//            uid = 1,
//            task_title = "hello world",
//            task_description = "hello world",
//            time_in_long = calendarTime.timeInMillis,
//        )
//        val intent = Intent(this, NotificationReceiver::class.java)
//        intent.putExtra("tableOfTask", tableOfTask)
//        val pendingIntent =
//            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
//
//        val alarmClockInfo: AlarmManager.AlarmClockInfo =
//            AlarmManager.AlarmClockInfo(calendarTime.timeInMillis, pendingIntent)
//        alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channelId = "notification_id_high"
        val channelName =
            "For showing notification"
        val channelDescription =
            "For showing notification"

        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        channel.description = channelDescription

        channel.lightColor = Color.Red.hashCode()
        channel.setSound(null, null)
        channel.enableVibration(true)
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
}

package com.example.notifyme.ui.utils.notification

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SYSTEM_EXEMPTED
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.AlertSoon.R
import com.example.AlertSoon.ui.local_storage.Task.TableOfTask
import com.example.AlertSoon.ui.utils.notification.AlarmMangerHandler
import com.example.AlertSoon.ui.utils.notification.NotificationReceiver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class ForegroundService : Service() {

    private val NOTIFICATION_ID = 123
    private val CHANNEL_ID = "ForegroundServiceChannel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        Log.e("AlertSoon", "foreground service is created")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, createNotification(), FOREGROUND_SERVICE_TYPE_SYSTEM_EXEMPTED)
        }
        else{
            startForeground(NOTIFICATION_ID, createNotification())
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if(intent?.action == "create_notification"){

            val tableOfTask: TableOfTask? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("tableOfTask", TableOfTask::class.java)
            } else {
                intent.getParcelableExtra("tableOfTask")
            }


            if(tableOfTask!=null){

                cancelAlarm(tableOfTask)


                val alarmManager =
                    getSystemService(Context.ALARM_SERVICE) as AlarmManager

                val intent = Intent(this, NotificationReceiver::class.java)
                intent.putExtra("tableOfTask", tableOfTask)

                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    tableOfTask.uid?.toInt()!!,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
                )


                alarmManager.setAlarmClock(
                    AlarmManager.AlarmClockInfo(
                        tableOfTask.snozze_time ?: tableOfTask.time_in_long!!,
                        pendingIntent
                    ), pendingIntent
                );

//                val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//                val pendingIntent = PendingIntent.getBroadcast(this, alarmManager.hashCode(), Intent(this, NotificationReceiver::class.java), PendingIntent.FLAG_IMMUTABLE)
//                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
                Log.e("ServiceApp", "Notification created successfully from foreground service")
            }
        }
        else if(intent?.action == "remove_notification") {
            val tableOfTask: TableOfTask? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("tableOfTask", TableOfTask::class.java)
            } else {
                intent.getParcelableExtra("tableOfTask")
            }
            if(tableOfTask!=null)
                cancelAlarm(tableOfTask)
        }
        return START_STICKY
    }

    private fun cancelAlarm(tableOfTask: TableOfTask) {

        val alarmManager =
            getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, NotificationReceiver::class.java)
        intent.putExtra("tableOfTask", tableOfTask)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            tableOfTask.uid?.toInt()!!,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun createNotification(): Notification {
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentText("Service is running in the foreground")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)

        return builder.build()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

    }
}
package com.example.AlertSoon.ui.utils.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.AlertSoon.ui.local_storage.Task.TableOfTask
import com.example.AlertSoon.ui.local_storage.Task.TaskRespository
import com.example.notifyme.ui.utils.notification.ForegroundService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject


class AlarmMangerHandler @Inject constructor(
    val context: Context
) {


    fun createAlarm(tableOfTask: TableOfTask? = null) {

        val serviceIntent = Intent(context, ForegroundService::class.java)
        serviceIntent.action = "create_notification"
        serviceIntent.putExtra("tableOfTask", tableOfTask)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
//        if (tableOfTask == null) {
//            return
//        }
//
//        cancelAlarm(context, tableOfTask)
//
//
//        val alarmManager =
//            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//        val intent = Intent(context, NotificationReceiver::class.java)
//        intent.putExtra("tableOfTask", tableOfTask)
//
//        val pendingIntent = PendingIntent.getBroadcast(
//            context,
//            tableOfTask.uid?.toInt()!!,
//            intent,
//            PendingIntent.FLAG_IMMUTABLE
//        )
//
//
//        alarmManager.setAlarmClock(
//            AlarmManager.AlarmClockInfo(
//                tableOfTask.snozze_time ?: tableOfTask.time_in_long!!,
//                pendingIntent
//            ), pendingIntent
//        );

        Log.e("AlertSoon", "alaram created sucessfully")
    }

    fun cancelAlarm(tableOfTask: TableOfTask) {

        val serviceIntent = Intent(context, ForegroundService::class.java)
        serviceIntent.action = "remove_notification"
        serviceIntent.putExtra("tableOfTask", tableOfTask)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
//        val alarmManager =
//            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//
//        val intent = Intent(context, NotificationReceiver::class.java)
//        intent.putExtra("tableOfTask", tableOfTask)
//
//        val pendingIntent = PendingIntent.getBroadcast(
//            context,
//            tableOfTask.uid?.toInt()!!,
//            intent,
//            PendingIntent.FLAG_IMMUTABLE
//        )
//
//        alarmManager.cancel(pendingIntent)
    }
}

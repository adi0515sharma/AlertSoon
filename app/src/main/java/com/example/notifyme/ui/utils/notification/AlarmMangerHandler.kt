package com.example.AlertSoon.ui.utils.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.AlertSoon.ui.local_storage.Task.TableOfTask
import com.example.AlertSoon.ui.local_storage.Task.TaskRespository
import com.example.AlertSoon.ui.utils.DateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject


class AlarmMangerHandler @Inject constructor(
    val context: Context,
    val taskRespository: TaskRespository
) {


    fun createAlarm(tableOfTask: TableOfTask? = null) {
        if (tableOfTask == null) {
            return
        }

        cancelAlarm(tableOfTask)


        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, NotificationReceiver::class.java)
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        intent.putExtra("tableOfTask", tableOfTask)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
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

        Log.e("AlertSoon", "alaram created sucessfully")
    }

    fun createSampleNotification(){
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, SampleNotificationReceiver::class.java)
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            intent.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )


        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(
                DateTime.getTimePlusMinutes(),
                pendingIntent
            ), pendingIntent
        );
    }
    fun cancelAlarm(tableOfTask: TableOfTask) {

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, NotificationReceiver::class.java)
        intent.putExtra("tableOfTask", tableOfTask)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            tableOfTask.uid?.toInt()!!,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
    }
}

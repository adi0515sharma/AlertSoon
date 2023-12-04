package com.example.notifyme.ui.utils.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.notifyme.ui.local_storage.Task.TableOfTask
import com.example.notifyme.ui.local_storage.Task.TaskRespository
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

        Log.e("NotifyMe", "alaram created sucessfully")
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

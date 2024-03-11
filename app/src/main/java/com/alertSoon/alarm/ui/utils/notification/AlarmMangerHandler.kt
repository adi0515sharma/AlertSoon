package com.alertSoon.alarm.ui.utils.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.number.IntegerWidth
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.alertSoon.alarm.ui.local_storage.Task.TableOfTask
import com.alertSoon.alarm.ui.local_storage.Task.TaskRespository
import com.alertSoon.alarm.ui.utils.DateTime.getNextTimeForRegularTask
import kotlinx.coroutines.runBlocking
import java.util.Calendar
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
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.e("AlertSoon","alarmManager.canScheduleExactAlarms() = ${alarmManager.canScheduleExactAlarms()}" )
            if (!alarmManager.canScheduleExactAlarms()) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, tableOfTask.snozze_time ?: tableOfTask.time_in_long!!, pendingIntent)
                return
            }
        }
        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(
                tableOfTask. snozze_time ?: tableOfTask.time_in_long!!,
                pendingIntent
            ), pendingIntent
        );

        Log.e("AlertSoon", "alaram created sucessfully")
    }


    fun cancelAlarm(tableOfTask: TableOfTask) {

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, NotificationReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            tableOfTask.uid?.toInt()!!,
            intent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.cancel(pendingIntent)
    }

    fun onDismissPress(tableOfTask: TableOfTask?) {
        if (tableOfTask == null) {
            return
        }
        if (tableOfTask.is_regular) {

            tableOfTask.time_in_long = tableOfTask.getNextTimeForRegularTask()
            tableOfTask.snozze_time = null
            createAlarm(tableOfTask)

            runBlocking {
                if(taskRespository.getTaskById(tableOfTask.uid!!) != null){
                    try {
                        taskRespository.updateTask(tableOfTask)


                    } catch (e: Exception) {
                        cancelAlarm(tableOfTask)
                    }
                }
                else{
                    cancelAlarm(tableOfTask)
                }


            }

        } else {
            runBlocking {
                taskRespository.deleteTaskById(tableOfTask.uid!!)
            }
        }

    }

    fun onSnoozePress(tableOfTask: TableOfTask?, itIsDone: (() -> Unit)?) {
        if (tableOfTask == null) {
            return
        }

        val snoozeTimeMillis: Long = Calendar.getInstance().timeInMillis + 10 * 60 * 1000 // Add 10 minutes (10 * 60 seconds * 1000 milliseconds)
        tableOfTask.snozze_time = snoozeTimeMillis

        createAlarm(tableOfTask)

        runBlocking {
            if(taskRespository.getTaskById(tableOfTask.uid!!) != null){
                try {
                    taskRespository.updateTask(tableOfTask)
                    itIsDone?.invoke()
                } catch (e: Exception) {
                    cancelAlarm(tableOfTask)
                }
            }
            else{
                cancelAlarm(tableOfTask)
            }



        }

    }



}

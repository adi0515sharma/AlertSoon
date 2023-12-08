package com.example.AlertSoon.ui.utils.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.AlertSoon.ui.local_storage.Task.TableOfTask
import com.example.AlertSoon.ui.screens.home_screen_activity.domain.repository.HomeScreenTaskRepository
import com.example.AlertSoon.ui.utils.ApiResponse
import com.example.AlertSoon.ui.utils.DateTime.getNextTimeForRegularTask
import com.example.AlertSoon.ui.utils.notification.NotificationReceiver.Companion.getNotificationAlarmRingtoneInstance
import com.example.notifyme.ui.utils.notification.ForegroundService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class NotificationActionHandler : BroadcastReceiver() {

    @Inject
    lateinit var homeScreenTaskRepository: HomeScreenTaskRepository

    @Inject
    lateinit var alarmMangerHandler: AlarmMangerHandler

    override fun onReceive(context: Context, intent: Intent) {
        Log.e("AlertSoon", "NotificationActionHandler start")


        val tableOfTask: TableOfTask? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("tableOfTask", TableOfTask::class.java) ?: return
        } else {
            intent.getParcelableExtra("tableOfTask") ?: return
        }

        if (tableOfTask == null)
            return

        getNotificationAlarmRingtoneInstance(tableOfTask.uid!!)?.stop()

        if (intent.action == Action.SNOOZE.name) {

            val snoozeTimeMillis: Long = Calendar.getInstance().timeInMillis + 10 * 60 * 1000 // Add 10 minutes (10 * 60 seconds * 1000 milliseconds)
            tableOfTask.snozze_time = snoozeTimeMillis

            val serviceIntent = Intent(context, ForegroundService::class.java)
            serviceIntent.action = "create_notification"
            serviceIntent.putExtra("tableOfTask", tableOfTask)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent)
            } else {
                context.startService(serviceIntent)
            }

            runBlocking {

                val snoozeResult = homeScreenTaskRepository.updateTask(tableOfTask)

                if (snoozeResult !is ApiResponse.Success) {
                    alarmMangerHandler.cancelAlarm(tableOfTask)
                }
            }


        } else if (intent.action == Action.REMOVE_ALL.name || intent.action == Action.DISMISS.name) {

            if (tableOfTask.is_regular) {

                tableOfTask.time_in_long = tableOfTask.getNextTimeForRegularTask()
                tableOfTask.snozze_time = null

                val serviceIntent = Intent(context, ForegroundService::class.java)
                serviceIntent.action = "create_notification"
                serviceIntent.putExtra("tableOfTask", tableOfTask)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(serviceIntent)
                } else {
                    context.startService(serviceIntent)
                }


                runBlocking {
                    val updateResponse = homeScreenTaskRepository.updateTask(tableOfTask)
                    if (updateResponse !is ApiResponse.Success) {
                        alarmMangerHandler.cancelAlarm(tableOfTask)
                    }
                }
            } else {
                runBlocking {
                    homeScreenTaskRepository.deleteTaskById(tableOfTask.uid)
                }
            }

        }


        val notificationId = intent.getIntExtra("notificationId", -1)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)

    }
}

enum class Action {
    SNOOZE,
    DISMISS,
    REMOVE_ALL
}
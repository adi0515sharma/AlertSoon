package com.alertSoon.alarm.ui.utils.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.alertSoon.alarm.ui.local_storage.Task.TableOfTask
import com.alertSoon.alarm.ui.screens.home_screen_activity.domain.repository.HomeScreenTaskRepository
import com.alertSoon.alarm.ui.utils.notification.NotificationReceiver.Companion.getNotificationAlarmRingtoneInstance
import dagger.hilt.android.AndroidEntryPoint
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

            alarmMangerHandler.onSnoozePress(tableOfTask, null)

        }
        else if (intent.action == Action.REMOVE_ALL.name || intent.action == Action.DISMISS.name) {

            alarmMangerHandler.onDismissPress(tableOfTask)
        }



        val notificationId = intent.getIntExtra("notificationId", -1)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
        Log.e("AlertSoon", "notification removed")

    }
}

enum class Action {
    SNOOZE,
    DISMISS,
    REMOVE_ALL
}
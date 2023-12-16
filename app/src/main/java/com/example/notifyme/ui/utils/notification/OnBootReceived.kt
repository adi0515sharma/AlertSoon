package com.example.AlertSoon.ui.utils.notification

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.AlertSoon.R
import com.example.AlertSoon.ui.local_storage.Task.TaskRespository
import com.example.AlertSoon.ui.screens.home_screen_activity.domain.repository.HomeScreenTaskRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class OnBootReceived : BroadcastReceiver() {

    @Inject
    lateinit var taskRepository: TaskRespository

    @Inject
    lateinit var alarmMangerHandler: AlarmMangerHandler

    override fun onReceive(context: Context?, intent: Intent?) {

        var notification = NotificationCompat.Builder(context!!, "notification_again")
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_high_priority_flag)
            .setContentTitle("Rescheduling")
            .setContentText("Again seting alarm")
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(System.currentTimeMillis().toInt(), notification)
        }

        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {



            Log.e("AlertSoon", "device restarted")
            runBlocking {
                val tasks = taskRepository.getAllTasks()
                tasks.forEach {
                    alarmMangerHandler.createAlarm(it)
                }
            }
        }
    }
}
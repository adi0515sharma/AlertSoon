package com.alertSoon.alarm.ui.utils.notification

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.alertSoon.alarm.R
import com.alertSoon.alarm.ui.local_storage.Task.TaskRespository
import com.alertSoon.alarm.ui.screens.home_screen_activity.domain.repository.HomeScreenTaskRepository
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



        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {

            runBlocking {
                val tasks = taskRepository.getAllTasks()
                tasks.forEach {
                    alarmMangerHandler.createAlarm(it)
                }
            }

        }
    }
}
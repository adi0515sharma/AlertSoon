package com.alertSoon.alarm.ui.local_storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alertSoon.alarm.ui.local_storage.Task.TableOfTask
import com.alertSoon.alarm.ui.local_storage.Task.TaskDao


@Database(entities = [TableOfTask::class], version = 1)
abstract class AlertSoonDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
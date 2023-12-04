package com.example.notifyme.ui.local_storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.notifyme.ui.local_storage.Task.TableOfTask
import com.example.notifyme.ui.local_storage.Task.TaskDao


@Database(entities = [TableOfTask::class], version = 1)
abstract class NotifyMeDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
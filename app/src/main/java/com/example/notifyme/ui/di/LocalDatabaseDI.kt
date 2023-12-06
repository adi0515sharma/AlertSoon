package com.example.AlertSoon.ui.di

import android.content.Context
import androidx.room.Room
import com.example.AlertSoon.ui.local_storage.AlertSoonDatabase
import com.example.AlertSoon.ui.local_storage.Task.TaskDao
import com.example.AlertSoon.ui.local_storage.Task.TaskRespository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class LocalDatabaseDI {

    @Provides
    fun getTaskDaoInstance(AlertSoonDatabase: AlertSoonDatabase) = AlertSoonDatabase.taskDao()

    @Provides
    fun getTaskTableRepositoryInstance(taskDao: TaskDao) = TaskRespository(taskDao)

}

@Module
@InstallIn(SingletonComponent::class)
object LocalDatabaseInstance {

    @Singleton
    @Provides
    fun getDataBaseInstance(@ApplicationContext applicationContext : Context) = Room.databaseBuilder(
        applicationContext,
        AlertSoonDatabase::class.java, "AlertSoonDB"
    ).build()
}


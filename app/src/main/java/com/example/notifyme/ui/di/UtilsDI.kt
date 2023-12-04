package com.example.notifyme.ui.di

import android.content.Context
import com.example.notifyme.ui.local_storage.Task.TaskRespository
import com.example.notifyme.ui.utils.notification.AlarmMangerHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
class UtilsDI {

    @Provides
    fun getAlarmMangerHandlerInstance(@ApplicationContext context: Context, taskRespository: TaskRespository) = AlarmMangerHandler(context, taskRespository)

    @Provides
    fun getContextInstance(@ApplicationContext context: Context) = context
}
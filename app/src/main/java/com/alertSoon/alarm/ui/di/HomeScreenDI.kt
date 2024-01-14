package com.alertSoon.alarm.ui.di

import com.alertSoon.alarm.ui.local_storage.Task.TaskRespository
import com.alertSoon.alarm.ui.screens.home_screen_activity.data.repository.HomeScreenTaskRepositoryImpl
import com.alertSoon.alarm.ui.screens.home_screen_activity.domain.repository.HomeScreenTaskRepository
import com.alertSoon.alarm.ui.utils.notification.AlarmMangerHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
class HomeScreenDI {

    @Provides
    fun getHomeScreenTaskRepositoryInstance(taskRespository: TaskRespository, alarmMangerHandler: AlarmMangerHandler) : HomeScreenTaskRepository = HomeScreenTaskRepositoryImpl(taskRespository, alarmMangerHandler)
}
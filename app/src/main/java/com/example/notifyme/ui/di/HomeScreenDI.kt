package com.example.AlertSoon.ui.di

import com.example.AlertSoon.ui.local_storage.Task.TaskRespository
import com.example.AlertSoon.ui.screens.home_screen_activity.data.repository.HomeScreenTaskRepositoryImpl
import com.example.AlertSoon.ui.screens.home_screen_activity.domain.repository.HomeScreenTaskRepository
import com.example.AlertSoon.ui.utils.notification.AlarmMangerHandler
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
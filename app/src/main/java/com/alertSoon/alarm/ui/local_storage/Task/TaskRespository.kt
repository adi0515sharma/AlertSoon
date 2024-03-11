package com.alertSoon.alarm.ui.local_storage.Task

import javax.inject.Inject

class TaskRespository @Inject constructor(val taskDao: TaskDao) {

    suspend fun insertTaskIntoTable(task: TableOfTask) : Long = taskDao.insertTask(task)

    suspend fun deleteTaskById(id : Long) = taskDao.deleteTaskById(id)

    suspend fun getTaskById(id : Long) = taskDao.getTaskById(id)

    suspend fun updateTask(task: TableOfTask) = taskDao.updateTask(task)
    suspend fun getAllTasks() = taskDao.getAllTasks()

    suspend fun snoozeTask(id : Long, snooze_time : Long?) = taskDao.snoozeTask(id, snooze_time)

    fun getNextFiveTasks() = taskDao.getNextFiveTask()

    fun getOnceTasks() = taskDao.getOnceTasks()

    fun getRegularTasks() = taskDao.getRegularTasks()
}
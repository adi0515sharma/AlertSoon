package com.example.AlertSoon.ui.local_storage.Task

import com.example.AlertSoon.ui.utils.DateTime.getDate
import com.example.AlertSoon.ui.utils.DateTime.getTime
import kotlinx.coroutines.flow.Flow
import java.util.Calendar
import javax.inject.Inject

class TaskRespository @Inject constructor(val taskDao: TaskDao) {

    suspend fun insertTaskIntoTable(task: TableOfTask) : Long = taskDao.insertTask(task)

    suspend fun deleteTaskById(id : Long) = taskDao.deleteTaskById(id)

    suspend fun getTaskById(id : Long) = taskDao.getTaskById(id)

    suspend fun updateTask(task: TableOfTask) = taskDao.updateTask(task)

    suspend fun snoozeTask(id : Long, snooze_time : Long?) = taskDao.snoozeTask(id, snooze_time)

    suspend fun getNextTask() = taskDao.getNextTask()

    fun getNextFiveTasks() = taskDao.getNextFiveTask()

    fun getOnceTasks() = taskDao.getOnceTasks()

    fun getRegularTasks() = taskDao.getRegularTasks()
}
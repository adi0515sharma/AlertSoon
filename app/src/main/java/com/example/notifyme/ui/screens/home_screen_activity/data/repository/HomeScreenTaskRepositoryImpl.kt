package com.example.notifyme.ui.screens.home_screen_activity.data.repository

import android.util.Log
import com.example.notifyme.ui.local_storage.Task.TableOfTask
import com.example.notifyme.ui.local_storage.Task.TaskRespository
import com.example.notifyme.ui.screens.home_screen_activity.domain.repository.HomeScreenTaskRepository
import com.example.notifyme.ui.utils.notification.AlarmMangerHandler
import com.example.notifyme.ui.utils.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class HomeScreenTaskRepositoryImpl @Inject constructor(
    val taskRespository: TaskRespository,
    val alarmMangerHandler: AlarmMangerHandler
) :
    HomeScreenTaskRepository {


    override suspend fun getTaskById(id: Long) :ApiResponse<TableOfTask?> {
        try {
            var tableOfTask = taskRespository.getTaskById(id)

            return ApiResponse.Success(tableOfTask)

        } catch (e: Exception) {
            Log.e("NotifyMe", "we found error")
            e.printStackTrace()
        }
        return ApiResponse.Error("Something went wrong")
    }

    override suspend fun insertTaskIntoTable(task: TableOfTask): ApiResponse<TableOfTask?> {

        try {
            val id: Long = taskRespository.insertTaskIntoTable(task = task)
            val fetchUpdatedTask = getTaskById(id)
            var tableOfTask : TableOfTask? = null
            if(fetchUpdatedTask is ApiResponse.Success){
                tableOfTask = fetchUpdatedTask.data
            }
            return ApiResponse.Success(tableOfTask)
        } catch (e: Exception) {
            Log.e("NotifyMe", "we found error")
            e.printStackTrace()
        }

        return ApiResponse.Error("Something went wrong")
    }

    override suspend fun deleteTaskById(id: Long) : ApiResponse<Nothing> {
        try {
            Log.e("NotifyMe", "delete = $id")
            taskRespository.deleteTaskById(id = id)
            return ApiResponse.Success(null)

        } catch (e: Exception) {
            Log.e("NotifyMe", "we found error")
            e.printStackTrace()
        }
        return ApiResponse.Error<Nothing>("Something went wrong")
    }

    override suspend fun updateTask(task: TableOfTask): ApiResponse<TableOfTask?> {
        try {

            taskRespository.updateTask(task)

            val fetchUpdatedTask = getTaskById(task.uid!!)
            var tableOfTask : TableOfTask? = null
            if(fetchUpdatedTask is ApiResponse.Success){
                tableOfTask = fetchUpdatedTask.data
            }
            return ApiResponse.Success(tableOfTask)

        } catch (e: Exception) {
            Log.e("NotifyMe", "we found error")
            e.printStackTrace()
        }
        return ApiResponse.Error("Something went wrong")
    }

    override suspend fun snoozeTask(id: Long, soonze_time: Long?): ApiResponse<Nothing> {

        try {

            taskRespository.snoozeTask(id = id, snooze_time = soonze_time)
            return ApiResponse.Success(null)

        } catch (e: Exception) {
            Log.e("NotifyMe", "we found error")
            e.printStackTrace()
        }
        return ApiResponse.Error("Something went wrong")
    }

    override fun getOnceTask() = taskRespository.getOnceTasks()

    override fun getRegularTask()= taskRespository.getRegularTasks()

    override fun getNextFiveTask()= taskRespository.getNextFiveTasks()


}
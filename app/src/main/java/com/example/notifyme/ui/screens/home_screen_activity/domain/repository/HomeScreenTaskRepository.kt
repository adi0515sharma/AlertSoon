package com.example.notifyme.ui.screens.home_screen_activity.domain.repository
import com.example.notifyme.ui.utils.ApiResponse
import com.example.notifyme.ui.local_storage.Task.TableOfTask
import kotlinx.coroutines.flow.Flow


interface HomeScreenTaskRepository {

    suspend fun getTaskById(id : Long): ApiResponse<TableOfTask?>
    suspend fun insertTaskIntoTable(task: TableOfTask) : ApiResponse<TableOfTask?>
    suspend fun deleteTaskById(id: Long) : ApiResponse<Nothing>
    suspend fun updateTask(tableOfTask: TableOfTask) : ApiResponse<TableOfTask?>
    suspend fun snoozeTask(id : Long, soonze_time : Long?) : ApiResponse<Nothing>

    fun getOnceTask() : Flow<MutableList<TableOfTask>>
    fun getRegularTask() : Flow<MutableList<TableOfTask>>
    fun getNextFiveTask() : Flow<MutableList<TableOfTask>>
}
package com.alertSoon.alarm.ui.screens.home_screen_activity.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alertSoon.alarm.ui.local_storage.Task.TableOfTask
import com.alertSoon.alarm.ui.screens.home_screen_activity.domain.repository.HomeScreenTaskRepository
import com.alertSoon.alarm.ui.utils.ApiResponse
import com.alertSoon.alarm.ui.utils.notification.AlarmMangerHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class HomeActivityViewModel @Inject constructor(val homeScreenTaskRepository: HomeScreenTaskRepository, val alarmMangerHandler: AlarmMangerHandler) :
    ViewModel() {


    private val _insert_task: MutableSharedFlow<ApiResponse<TableOfTask?>> = MutableSharedFlow<ApiResponse<TableOfTask?>>()
    val insert_task: SharedFlow<ApiResponse<TableOfTask?>> = _insert_task

    private val _delete_task: MutableSharedFlow<ApiResponse<Nothing>> = MutableSharedFlow()
    val delete_task: SharedFlow<ApiResponse<Nothing>> = _delete_task

    private val _update_task: MutableSharedFlow<ApiResponse<TableOfTask?>> = MutableSharedFlow()
    val update_task: SharedFlow<ApiResponse<TableOfTask?>> = _update_task

    private val _single_task: MutableStateFlow<ApiResponse<TableOfTask?>> = MutableStateFlow(ApiResponse.Loading(null))
    val single_task: StateFlow<ApiResponse<TableOfTask?>> = _single_task

    private val _next_five_tasks: MutableStateFlow<MutableList<TableOfTask>> = MutableStateFlow(mutableListOf())
    val next_five_tasks: StateFlow<MutableList<TableOfTask>> = _next_five_tasks

    private var _once_tasks: MutableStateFlow<MutableList<TableOfTask>> = MutableStateFlow(mutableListOf())
    val once_tasks: StateFlow<MutableList<TableOfTask>> = _once_tasks

    private val _regular_tasks: MutableStateFlow<MutableList<TableOfTask>> = MutableStateFlow(mutableListOf())
    val regular_tasks: StateFlow<MutableList<TableOfTask>> = _regular_tasks


//    private val _cuOperation = MutableStateFlow<ApiResponse<TableOfTask?>?>(ApiResponse.Loading(null))
//    var cuOperation : ApiResponse<TableOfTask?>? = null



    init {
        getRegularTasks()
        getOnceTasks()
        getNextFiveTasks()
    }

    fun insertTask(task: TableOfTask) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.e("AlertSoon", "----------------------- insert task function ----------------------")

            val insertResult = homeScreenTaskRepository.insertTaskIntoTable(task)
            _insert_task.emit(insertResult)
        }
    }

    fun updateTask(task: TableOfTask) {

        viewModelScope.launch(Dispatchers.IO) {
            val updateResult = homeScreenTaskRepository.updateTask(task)
            _update_task.emit(updateResult)
        }
    }

    fun deleteTask(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {

            val responseTask = homeScreenTaskRepository.getTaskById(id)
            if(responseTask is ApiResponse.Success){
                withContext(Dispatchers.Main){
                    alarmMangerHandler.cancelAlarm(responseTask.data!!)
                }
            }
            val deleteResponse = homeScreenTaskRepository.deleteTaskById(id)
            _delete_task.emit(deleteResponse)

        }
    }

    fun getTaskById(id: Int) {

        Log.e("AlertSoon", "-------- fetching id = $id task ----------")
        viewModelScope.launch(Dispatchers.IO) {
            _single_task.emit(ApiResponse.Loading(null))
            val responseOfTask = homeScreenTaskRepository.getTaskById(id.toLong())
            _single_task.emit(responseOfTask)
        }
    }


    fun getNextFiveTasks(){

        viewModelScope.launch(Dispatchers.IO){
            try {
                homeScreenTaskRepository.getNextFiveTask().collectLatest { it ->
                    Log.e("AlertSoon", "getOnceTasks...emitting: ${it.javaClass.simpleName}")
                    _next_five_tasks.emit(it)
                }
            } catch (e: Exception) {
                Log.e("AlertSoon", "Error in getOnceTasks: ${e.message}", e)
                // Handle error here if needed
            }
        }

    }

    fun getOnceTasks(){
        Log.e("AlertSoon", "getOnceTask")
        viewModelScope.launch(Dispatchers.IO){
            try {
                homeScreenTaskRepository.getOnceTask().collectLatest { it ->
                    Log.e("AlertSoon", "getOnceTasks...emitting: ${it.javaClass.simpleName}")
                    _once_tasks.emit(it)
                }
            } catch (e: Exception) {
                Log.e("AlertSoon", "Error in getOnceTasks: ${e.message}", e)
                // Handle error here if needed
            }
        }
    }

    fun getRegularTasks(){

        viewModelScope.launch(Dispatchers.IO){
            try {
                homeScreenTaskRepository.getRegularTask().collectLatest { it ->
                    Log.e("AlertSoon", "getOnceTasks...emitting: ${it.javaClass.simpleName}")
                    _regular_tasks.emit(it)
                }
            } catch (e: Exception) {
                Log.e("AlertSoon", "Error in getOnceTasks: ${e.message}", e)
                // Handle error here if needed
            }
        }
    }


}
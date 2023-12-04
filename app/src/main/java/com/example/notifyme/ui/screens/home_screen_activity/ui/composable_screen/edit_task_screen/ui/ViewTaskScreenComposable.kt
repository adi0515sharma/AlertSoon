package com.example.notifyme.ui.screens.home_screen_activity.ui.composable_screen.edit_task_screen.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.notifyme.ui.local_storage.Task.TableOfTask
import com.example.notifyme.ui.screens.home_screen_activity.ui.HomeActivityViewModel
import com.example.notifyme.ui.screens.home_screen_activity.ui.composable_screen.create_task_screen.ui.Screen
import com.example.notifyme.ui.utils.ApiResponse
import kotlinx.coroutines.flow.collectLatest


@Composable
fun ViewTaskScreenComposable(
    navController: NavHostController,
    tableOfTask : TableOfTask?,
    updateStateListener : (TableOfTask) -> Unit,
    updateTaskListener : (TableOfTask) -> Unit
) {


    Screen(navController = navController, tableOfTask = tableOfTask, updateStateListener = updateStateListener) { it ->
        Log.e("NotifyMe", it.toString())
        updateTaskListener(it)
    }

}


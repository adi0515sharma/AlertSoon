package com.example.AlertSoon.ui.navigation

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.AlertSoon.ui.component.IssueExecution
import com.example.AlertSoon.ui.local_storage.Task.TableOfTask
import com.example.AlertSoon.ui.screens.home_screen_activity.ui.HomeActivityViewModel
import com.example.AlertSoon.ui.screens.home_screen_activity.ui.composable_screen.create_task_screen.ui.CreateTaskComposable
import com.example.AlertSoon.ui.screens.home_screen_activity.ui.composable_screen.create_task_screen.ui.SystemRingtoneScreen
import com.example.AlertSoon.ui.screens.home_screen_activity.ui.composable_screen.edit_task_screen.ui.ViewTaskScreenComposable
import com.example.AlertSoon.ui.screens.home_screen_activity.ui.composable_screen.home_screen.ui.HomeScreenComposable
import com.example.AlertSoon.ui.utils.ApiResponse
import com.example.AlertSoon.ui.utils.notification.AlarmMangerHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import java.net.URLDecoder


@Composable
fun HomeScreenNavHost(
    alarmMangerHandler: AlarmMangerHandler,
    askForPermission: () -> Boolean,
    issueExecution: IssueExecution
) {
    val navController = rememberNavController()

    val viewModel: HomeActivityViewModel = viewModel()

    NavHost(navController, startDestination = FeatureNavScreen.HOME.name) {

        composable(route = FeatureNavScreen.HOME.name) {

            HomeScreenComposable(
                navController,
                viewModel, { it, type ->
                    viewModel.deleteTask(it)
                },
                issueExecution
            )
        }
        composable(route = FeatureNavScreen.CREATING_TASK.name) {
            var tableOfTask by rememberSaveable { mutableStateOf(TableOfTask()) }


            val context: Context = LocalContext.current


            var shouldCreateAlarm by rememberSaveable {
                mutableStateOf<TableOfTask?>(null)
            }


            LaunchedEffect(key1 = shouldCreateAlarm) {
                withContext(Dispatchers.Main) {
                    alarmMangerHandler.createAlarm(shouldCreateAlarm)
                }
            }

            DisposableEffect(navController) {

                val callback = NavController.OnDestinationChangedListener { _, _, _ ->
                    // Access the result from destination B

                    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

                    if(savedStateHandle?.contains("selectedUri") == true){
                        val sound = savedStateHandle.get<String>("selectedUri")
                        Log.e("AlertSoon", "selected sound is $sound")
                        tableOfTask = tableOfTask.copy(sound = sound)

                    }


                }

                // Add the listener
                navController.addOnDestinationChangedListener(callback)

                // Remove the listener when the composable is disposed
                onDispose {
                    navController.removeOnDestinationChangedListener(callback)
                }
            }


            LaunchedEffect(key1 = null) {
                viewModel.insert_task.collectLatest {
                    when (it) {
                        is ApiResponse.Success -> {
                            shouldCreateAlarm = it.data
                            Log.e("AlertSoon", "inserted successfully")
                            Toast.makeText(context, "successfully inserted", Toast.LENGTH_LONG)
                                .show()
                            navController.navigateUp()
                        }

                        is ApiResponse.Loading -> {
                        }

                        is ApiResponse.Error -> {
                            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                        }

                        else -> {}
                    }
                }
            }

            CreateTaskComposable(
                navController,
                tableOfTask,
                {
                    tableOfTask = it
                }
            ) {

                if (askForPermission())
                    viewModel.insertTask(it)
            }
        }
        composable(
            route = FeatureNavScreen.VIEW_TASK.name,
            arguments = listOf(navArgument("id") { type = NavType.IntType },
                navArgument("type") { type = NavType.StringType })
        ) { backStackEntry ->
            val context: Context = LocalContext.current

            val id = backStackEntry.arguments?.getInt("id") ?: return@composable

            var tableOfTask by rememberSaveable { mutableStateOf(TableOfTask()) }

            var shouldCreateAlarm by rememberSaveable { mutableStateOf<TableOfTask?>(null) }

            var alreadyFetched by rememberSaveable { mutableStateOf(false) }

            LaunchedEffect(key1 = shouldCreateAlarm) {

                withContext(Dispatchers.Main) {
                    alarmMangerHandler.createAlarm(shouldCreateAlarm)
                }

            }

            LaunchedEffect(key1 = null) {
                viewModel.single_task.collectLatest {
                    when (it) {
                        is ApiResponse.Success -> {
                            if (alreadyFetched) {
                                return@collectLatest
                            }
                            Log.e("AlertSoon", "select item : ${it.data!!.toString()}")
                            alreadyFetched = true
                            tableOfTask = it.data
                        }

                        is ApiResponse.Loading -> {
                        }

                        is ApiResponse.Error -> {
                        }

                        else -> {}
                    }
                }
            }

            LaunchedEffect(key1 = null) {
                viewModel.getTaskById(id)
            }


            LaunchedEffect(key1 = null) {
                viewModel.update_task.collectLatest {
                    when (it) {
                        is ApiResponse.Success -> {
                            shouldCreateAlarm = it.data
                            Toast.makeText(context, "update successfully", Toast.LENGTH_LONG).show()
                            navController.navigateUp()
                        }

                        is ApiResponse.Loading -> {
                            Log.e("AlertSoon", "update loading")
                        }

                        is ApiResponse.Error -> {
                            Log.e("AlertSoon", "update error")
                            Toast.makeText(context, "update failed", Toast.LENGTH_LONG).show()
                        }

                        else -> {}
                    }
                }
            }

            DisposableEffect(navController) {

                val callback = NavController.OnDestinationChangedListener { _, _, _ ->
                    // Access the result from destination B

                    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

                    if(savedStateHandle?.contains("selectedUri") == true){
                        val sound = savedStateHandle.get<String>("selectedUri")
                        Log.e("AlertSoon", "selected sound is $sound")
                        tableOfTask = tableOfTask.copy(sound = sound)

                    }


                }

                // Add the listener
                navController.addOnDestinationChangedListener(callback)

                // Remove the listener when the composable is disposed
                onDispose {
                    navController.removeOnDestinationChangedListener(callback)
                }
            }

            ViewTaskScreenComposable(
                navController,
                tableOfTask,
                {
                    tableOfTask = it
                }
            ) {
                if (askForPermission())
                    viewModel.updateTask(it)
            }
        }

        composable(
            route = FeatureNavScreen.SELECT_RINGTONE.name,
            arguments = listOf(navArgument("selectedUri") { type = NavType.StringType })
        ) { backStackEntry ->

            val selectedUri = if (backStackEntry.arguments?.containsKey("selectedUri") == true) {
                URLDecoder.decode(backStackEntry.arguments?.getString("selectedUri"), "UTF-8")
                    .replace(" ", "%20")
            } else {
                ""
            }

//            Log.e("AlertSoon", "selectedUri === ${selectedUri}")
            SystemRingtoneScreen(navController, selectedUri)

        }

    }
}


sealed class FeatureNavScreen(val name: String) {
    object HOME : FeatureNavScreen("HOME")
    object CREATING_TASK : FeatureNavScreen("CREATING_TASK")
    object VIEW_TASK : FeatureNavScreen("VIEW_TASK/{id}/{type}")
    object SELECT_RINGTONE : FeatureNavScreen("SELECT_SCREEN?selectedUri={selectedUri}")
}

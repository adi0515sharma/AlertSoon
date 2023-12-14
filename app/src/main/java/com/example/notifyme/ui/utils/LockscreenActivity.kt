package com.example.notifyme.ui.utils

import android.app.KeyguardManager
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.AlertSoon.R
import com.example.AlertSoon.ui.component.AppButton
import com.example.AlertSoon.ui.component.HandleClick
import com.example.AlertSoon.ui.local_storage.Task.TableOfTask
import com.example.AlertSoon.ui.screens.home_screen_activity.domain.repository.HomeScreenTaskRepository
import com.example.AlertSoon.ui.theme.AlertSoonTheme
import com.example.AlertSoon.ui.utils.ApiResponse
import com.example.AlertSoon.ui.utils.notification.AlarmMangerHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LockscreenActivity : ComponentActivity() {



    @Inject lateinit var homeScreenTaskRepository: HomeScreenTaskRepository

    @Inject lateinit var alarmMangerHandler: AlarmMangerHandler

    var isSnoozed = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tableOfTask: TableOfTask? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra("tableOfTask", TableOfTask::class.java)
        } else {
            intent?.getParcelableExtra("tableOfTask")
        }


        lifecycleScope.launch {
            val apiResponse = homeScreenTaskRepository.getTaskById(tableOfTask?.uid!!)

            if(apiResponse is ApiResponse.Success){
                Log.e("AlertSoon", "data from lock screen = ${apiResponse.data}")
            }
            else{
                Log.e("AlertSoon", "data from lock screen = false")
            }
        }
        setContent {
            AlertSoonTheme {
                var task by rememberSaveable { mutableStateOf(tableOfTask) }
                Surface {
                    Screen(task)
                }
            }
        }

        turnScreenOnAndKeyguardOff()
    }


    fun dismiss(){
        val tableOfTask: TableOfTask? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra("tableOfTask", TableOfTask::class.java) ?: return
        } else {
            intent?.getParcelableExtra("tableOfTask") ?: return
        }
        alarmMangerHandler.onDismissPress(tableOfTask)

    }
    fun snooze(){

        val tableOfTask: TableOfTask? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra("tableOfTask", TableOfTask::class.java) ?: return
        } else {
            intent?.getParcelableExtra("tableOfTask") ?: return
        }

        alarmMangerHandler.onSnoozePress(tableOfTask) {
            isSnoozed = true
            finish()
        }


    }
    @Composable
    fun Screen(tableOfTask: TableOfTask?){
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Time's Up",
                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                fontSize = 17.sp,
                fontWeight = FontWeight.W700
            )
            Text(
                text = tableOfTask?.leadIcon?:"",
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    ),
                ),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                fontSize = 120.sp,
            )
            Text(
                text = tableOfTask?.task_title?:"",
                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                fontSize = 17.sp,
                fontWeight = FontWeight.W700
            )
            Text(
                text = tableOfTask?.task_description?:"",
                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                fontSize = 17.sp,
                fontWeight = FontWeight.W700
            )

            AppButton(Modifier.fillMaxWidth(),"Snooze by 10 minute", object : HandleClick {
                override fun onClick() {
                    snooze()
                }
            })

            AppButton(Modifier.fillMaxWidth(),"Dismiss", object : HandleClick {
                override fun onClick() {
                    finish()
                }
            })
        }
    }

    fun removeNotification(){
        val notificationId = intent.getIntExtra("notificationId", -1)

        Log.e("AlertSoon", "removeNotification $notificationId")
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }
    override fun onDestroy() {
        super.onDestroy()
        turnScreenOffAndKeyguardOn()

        removeNotification()
        if(isSnoozed){
            return
        }
        dismiss()

    }

    private fun turnScreenOnAndKeyguardOff() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            )
        }

        with(getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requestDismissKeyguard(this@LockscreenActivity, null)
            }
        }
    }

    private fun turnScreenOffAndKeyguardOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(false)
            setTurnScreenOn(false)
        } else {
            window.clearFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            )
        }
    }


}
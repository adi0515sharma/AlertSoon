package com.alertSoon.alarm.ui.screens.lock_screen_activity

import android.app.KeyguardManager
import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.alertSoon.alarm.ui.component.AppButton
import com.alertSoon.alarm.ui.component.HandleClick
import com.alertSoon.alarm.ui.local_storage.Task.TableOfTask
import com.alertSoon.alarm.ui.screens.home_screen_activity.domain.repository.HomeScreenTaskRepository
import com.alertSoon.alarm.ui.theme.AlertSoonTheme
import com.alertSoon.alarm.ui.theme.dimens
import com.alertSoon.alarm.ui.utils.notification.AlarmMangerHandler
import com.alertSoon.alarm.ui.utils.notification.NotificationReceiver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
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


    private fun dismiss(){
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
            Toast.makeText(this, "Snoozed SuccessFully", Toast.LENGTH_LONG).show()
            finish()
        }


    }
    @Composable
    fun Screen(tableOfTask: TableOfTask?){
        ConstraintLayout(modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.dimens.lock_screen_horizontal_padding, vertical = MaterialTheme.dimens.lock_screen_vertical_padding)) {

            val (title, data, button) = createRefs()

            Column(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .height(MaterialTheme.dimens.lock_screen_times_up_height),

                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Time's Up",
                    textAlign = TextAlign.Center,
                    fontFamily = MaterialTheme.typography.displayMedium.fontFamily,
                    fontSize = MaterialTheme.typography.displayMedium.fontSize,
                    fontWeight = MaterialTheme.typography.displayMedium.fontWeight,
                    color = MaterialTheme.colorScheme.onBackground

                )
            }


            Column(modifier = Modifier.constrainAs(data){
                height = Dimension.fillToConstraints
                top.linkTo(title.bottom)
                bottom.linkTo(button.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, verticalArrangement = Arrangement.SpaceEvenly, horizontalAlignment = Alignment.CenterHorizontally ) {
//
                RippleLoadingAnimation(tableOfTask?.leadIcon?:"🎯")

                Text(
                    text =  tableOfTask?.task_title?:"",
                    fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )


            }

            Column(modifier = Modifier
                .constrainAs(button) {
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth()) {
                AppButton(Modifier.fillMaxWidth(),"Snooze by 10 minute", object : HandleClick {
                    override fun onClick() {
                        snooze()
                    }
                })

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.lock_screen_spacer))
                AppButton(Modifier.fillMaxWidth(),"Dismiss", object : HandleClick {
                    override fun onClick() {
                        finish()
                    }
                })
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.lock_screen_spacer))
            }

        }
    }
    @Composable
    fun RippleLoadingAnimation(icon : String) {


        val waves = listOf(
            remember { Animatable(0f) },
            remember { Animatable(0f) },
            remember { Animatable(0f) },
            remember { Animatable(0f) },
        )

        val animationSpec = infiniteRepeatable<Float>(
            animation = tween(4000, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Restart,
        )

        waves.forEachIndexed { index, animatable ->
            LaunchedEffect(animatable) {
                delay(index * 1000L)
                animatable.animateTo(
                    targetValue = 1f, animationSpec = animationSpec
                )
            }
        }

        val dys = waves.map { it.value }

        Box(
            modifier = Modifier.size(MaterialTheme.dimens.lock_height),
            contentAlignment = Center
        ) {
            // Waves
            dys.forEach { dy ->
                Box(
                    Modifier
                        .size(MaterialTheme.dimens.lock_wave_height)
                        .align(Alignment.Center)
                        .graphicsLayer {
                            scaleX = dy * 7 + 1
                            scaleY = dy * 7 + 1
                            alpha = 1 - dy
                        },
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                    )
                }
            }

            Box(
                Modifier
                    .size(MaterialTheme.dimens.lock_icon_size)
                    .align(Alignment.Center)
                    .background(color = Color.Transparent, shape = CircleShape)
            ) {

                Text(
                    text = icon,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        ),
                    ),

                    fontSize = MaterialTheme.typography.displayLarge.fontSize,
                            modifier = Modifier
                        .align(Alignment.Center)
                )
            }

        }
    }
    private fun removeNotification(){
        val notificationId = intent.getIntExtra("notificationId", -1)
        Log.e("AlertSoon", "removeNotification $notificationId")
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)

        val tableOfTask: TableOfTask? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra("tableOfTask", TableOfTask::class.java) ?: return
        } else {
            intent?.getParcelableExtra("tableOfTask") ?: return
        }


        NotificationReceiver.getNotificationAlarmRingtoneInstance(tableOfTask?.uid!!)?.stop()

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

    @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
    @Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showSystemUi = true)
    @Composable
    fun previewOfLockScreen(){


//        RippleLoadingAnimation()
        Screen(tableOfTask = TableOfTask(leadIcon = "😍", task_title = "How the josh", task_description = "hhj  ssknks knkns m sk k ks k s k s   skmkm"))
    }



}
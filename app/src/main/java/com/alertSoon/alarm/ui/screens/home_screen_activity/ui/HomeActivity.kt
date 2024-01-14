package com.alertSoon.alarm.ui.screens.home_screen_activity.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.alertSoon.alarm.R
import com.alertSoon.alarm.ui.navigation.HomeScreenNavHost
import com.alertSoon.alarm.ui.theme.AlertSoonTheme
import com.alertSoon.alarm.ui.utils.notification.AlarmMangerHandler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    @Inject
    lateinit var alarmMangerHandler: AlarmMangerHandler

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            AlertSoonTheme {
                var someSetting by rememberSaveable { mutableStateOf(false) }

                Surface() {


                    HomeScreenNavHost(
                        alarmMangerHandler,
                    ) {
                        checkNotification()


                    }


                }
            }
        }


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECEIVE_BOOT_COMPLETED
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECEIVE_BOOT_COMPLETED),
                12
            )
        }


    }



    private fun showAlertForNotificationPermission() {
        val builder = AlertDialog.Builder(this)

        // Set the message
        builder.setMessage("Please allow notification permission from settings. it will help you to notify your alarm.")

        // Set a title (optional)
        builder.setTitle(resources.getString(R.string.app_name))

        // Set a positive button with a click listener
        builder.setPositiveButton("Settings") { dialog, _ ->

            val packageName = "com.alertSoon.alarm"
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", packageName, null)
            intent.data = uri

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                // Handle the case where there is no activity to handle the intent
            }

            // Do something when the "OK" button is clicked (if needed)
            dialog.dismiss() // Dismiss the dialog
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->

            // Do something when the "OK" button is clicked (if needed)
            dialog.dismiss() // Dismiss the dialog
        }


        // Create and show the AlertDialog
        val dialog = builder.create()
        dialog.show()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            showAlertForNotificationPermission()
        }
    }


    private fun checkNotification(): Boolean {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED -> {

                return true;
            }

            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                showAlertForNotificationPermission()

            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        return false
    }


}




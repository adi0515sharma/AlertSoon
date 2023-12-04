package com.example.notifyme.ui.screens.home_screen_activity.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.example.notifyme.R
import com.example.notifyme.ui.component.IssueExecution
import com.example.notifyme.ui.local_storage.Task.TaskRespository
import com.example.notifyme.ui.navigation.HomeScreenNavHost
import com.example.notifyme.ui.screens.auth_screens_feature.data.api.BasicFirebaseAuth
import com.example.notifyme.ui.theme.NotifyMeTheme
import com.example.notifyme.ui.utils.NotifyMeSharePref
import com.example.notifyme.ui.utils.notification.AlarmMangerHandler
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    @Inject
    lateinit var googleAuthUiClient: BasicFirebaseAuth

    @Inject
    lateinit var taskRespository: TaskRespository

    @Inject
    lateinit var alarmMangerHandler: AlarmMangerHandler

    @Inject
    lateinit var notifyMeSharePref: NotifyMeSharePref

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            NotifyMeTheme {
                var someSetting by rememberSaveable { mutableStateOf(false) }

                Surface() {


                    HomeScreenNavHost(
                        alarmMangerHandler,
                        {
                            checkNotification()
                        },
                        object : IssueExecution {
                            override fun performSomeSettingTask() {
                                someSetting = true
                            }

                            override fun performNotificationAllowence() {
                                if(checkNotification()){
                                    Toast.makeText(this@HomeActivity , "permission is given , you can close this issue.", Toast.LENGTH_LONG).show()
                                }
                            }

                            override fun performBatteryConsumption() {
                                if (!(getSystemService(POWER_SERVICE) as PowerManager).isIgnoringBatteryOptimizations(
                                        packageName
                                    )
                                ) {
                                    val packageName = packageName
                                    val intent = Intent()
                                    intent.action =
                                        Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                                    intent.data = Uri.parse("package:$packageName")
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(
                                        this@HomeActivity,
                                        "permission is given , you can close this issue.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                        })

                    if (someSetting)
                    {
                        if(!isNetworkConnected(this)){
                            Toast.makeText(this, "No internet available", Toast.LENGTH_LONG).show()
                        }
                        else{
                            ShowOtherSettingPopup { someSetting = it }
                        }

                    }
                }
            }
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

            val packageName = "com.example.notifyme"
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

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkNotification(): Boolean {
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

    @Composable
    fun ShowOtherSettingPopup(setShowDialog: (Boolean) -> Unit) {

        var instruction by rememberSaveable {
            mutableStateOf<String?>(null)
        }

        val context = LocalContext.current
        LaunchedEffect(key1 = null) {
             fetchOtherSetting {


                 val final = it!!.trimIndent().replace("\\n", "\r\n\n")
                 if(final==""){
                     Toast.makeText(context, "you can close this issue.", Toast.LENGTH_LONG).show()
                 }
                 instruction = final
                 Log.e("NotifyMe", "instruction = ${instruction!!}")


            }
        }

        Dialog(
            onDismissRequest = { setShowDialog(false) },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                when (instruction) {
                    null -> {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "Loading...",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontFamily = FontFamily(Font(R.font.poppins_regular))
                            )
                        }

                    }
                    "" -> {
                        setShowDialog(false)
                    }
                    else -> {

                        Column(modifier = Modifier.padding(20.dp).verticalScroll(rememberScrollState())) {
                            Text(text = instruction!!, color = MaterialTheme.colorScheme.onBackground, fontFamily = FontFamily(Font(R.font.poppins_regular)))
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = "Cancel",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(Font(R.font.poppins_regular)),
                                    fontSize = 15.sp,
                                    modifier = Modifier.clickable {
                                        setShowDialog(false)
                                    }
                                )
                            }
                        }


                    }
                }
            }
        }
    }

    fun fetchOtherSetting(setInstruction: (String?) -> Unit){

        val androidVersion = Build.VERSION.RELEASE
        val manufacturer = Build.MANUFACTURER

        Log.d("NotifyMe", "Android Version: $androidVersion")
        Log.d("NotifyMe", "Manufacturer: $manufacturer")

            runBlocking {
                val db = Firebase.firestore
                db.collection("all_devices")
                    .document(manufacturer.lowercase())
                    .get()
                    .addOnSuccessListener { result ->
                        val instruction = result.data?.get(androidVersion)
                        if (instruction != null)
                            setInstruction(instruction as String)
                        else
                            setInstruction("")
                        Log.e("NotifyMe", "$instruction")
                    }
                    .addOnFailureListener { exception ->
                        Log.e("NotifyMe", "Error getting documents.", exception)
                        setInstruction("")
                    }
            }


    }

    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            // Check for Wi-Fi
            val wifiNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            if (wifiNetwork != null && wifiNetwork.isConnected) {
                return true
            }

            // Check for mobile data
            val mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            return mobileNetwork != null && mobileNetwork.isConnected
        }


        return false
    }
}




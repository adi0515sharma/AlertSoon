package com.example.notifyme.ui.utils.notification

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.AUDIO_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notifyme.R
import com.example.notifyme.ui.local_storage.Task.TableOfTask
import com.example.notifyme.ui.screens.home_screen_activity.domain.repository.HomeScreenTaskRepository
import com.example.notifyme.ui.utils.ApiResponse
import com.example.notifyme.ui.utils.DateTime
import com.example.notifyme.ui.utils.WakeLocker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {



        val tableOfTask: TableOfTask? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra("tableOfTask", TableOfTask::class.java) ?: return
        } else {
            intent?.getParcelableExtra("tableOfTask") ?: return
        }

        if(tableOfTask == null)
        {
            Log.e("NotifyMe", "is tableOfTask == null")
            return
        }

        Log.e("NotifyMe", "notification id = ${tableOfTask.uid}")

        val id = System.currentTimeMillis().toInt()

        val closeNotificationIntent = Intent(context, NotificationActionHandler::class.java)
        closeNotificationIntent.action = Action.DISMISS.toString()
        closeNotificationIntent.putExtra("notificationId", id)
        closeNotificationIntent.putExtra("tableOfTask", tableOfTask)
        val closeNotificationPendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            closeNotificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val snoozeNotificationIntent =
            Intent(context, NotificationActionHandler::class.java)
        snoozeNotificationIntent.action = Action.SNOOZE.toString()
        snoozeNotificationIntent.putExtra("notificationId", id)
        snoozeNotificationIntent.putExtra("tableOfTask", tableOfTask)

        val snoozeNotificationPendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            snoozeNotificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val onCancelIntent = Intent(context, NotificationActionHandler::class.java)
        onCancelIntent.putExtra("notificationId", id)
        onCancelIntent.putExtra("tableOfTask", tableOfTask);

        onCancelIntent.action = Action.REMOVE_ALL.toString()
        val onDismissPendingIntent = PendingIntent.getBroadcast(
            context,
            id,
            onCancelIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val bigNotificationLayout =
            RemoteViews(context!!.packageName, R.layout.notification_layout_big)
        bigNotificationLayout.setTextViewText(R.id.title, tableOfTask.task_title)
        bigNotificationLayout.setTextViewText(R.id.text, tableOfTask.task_description)
        bigNotificationLayout.setTextViewText(R.id.emoji, tableOfTask.leadIcon)
        bigNotificationLayout.setOnClickPendingIntent(
            R.id.dismiss,
            closeNotificationPendingIntent
        )
        bigNotificationLayout.setOnClickPendingIntent(
            R.id.snooze,
            snoozeNotificationPendingIntent
        )

        val smallNotificationLayout =
            RemoteViews(context.packageName, R.layout.notification_layout_small)
        smallNotificationLayout.setTextViewText(R.id.title, tableOfTask.task_title)
        smallNotificationLayout.setTextViewText(R.id.text, tableOfTask.task_description)
        smallNotificationLayout.setTextViewText(R.id.emoji, tableOfTask.leadIcon)
        smallNotificationLayout.setOnClickPendingIntent(
            R.id.dismiss,
            closeNotificationPendingIntent
        )
        smallNotificationLayout.setOnClickPendingIntent(
            R.id.snooze,
            snoozeNotificationPendingIntent
        )

        Log.e("NotifyMe", "launching notification ")


        val notification = NotificationCompat.Builder(context, "notification_id_high")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_high_priority_flag)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setCustomBigContentView(smallNotificationLayout)
                .setCustomContentView(smallNotificationLayout)
                .setContent(smallNotificationLayout)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCustomHeadsUpContentView(smallNotificationLayout)
                .setDeleteIntent(onDismissPendingIntent)
                .build()

        Log.e("NotifyMe", "notification id create $id")

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }


         NotificationManagerCompat.from(context).notify(id, notification)


        var selectedRingtoneUri =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        if (tableOfTask.sound != null && tableOfTask.sound!!.isNotEmpty()) {
            selectedRingtoneUri = Uri.parse(tableOfTask.sound)
        }
        val ringtone = RingtoneManager.getRingtone(context, selectedRingtoneUri)
        ringtone.play()
        setNotificationAlarmRingtone(tableOfTask.uid!!, ringtone)

        Log.e("NotifyMe", "launching notification succesfully")


    }


    companion object {

        private val notificationAlarm = hashMapOf<Long, Ringtone>()

        fun setNotificationAlarmRingtone(id: Long, ringtone: Ringtone) {

            if (notificationAlarm.containsKey(id)) {
                notificationAlarm.remove(id)
            }

            notificationAlarm[id] = ringtone
        }

        fun getNotificationAlarmRingtoneInstance(id: Long) = notificationAlarm[id]

        fun disableAllNotification() {
            for (ringtone in notificationAlarm.values) {
                if (ringtone.isPlaying) {
                    ringtone.stop()
                }
            }
        }
    }

    private fun increaseVolumn(context: Context?) {

        val audioManager = context?.getSystemService(AUDIO_SERVICE) as AudioManager

        val maxRingtoneVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING)
        audioManager.setStreamVolume(AudioManager.STREAM_RING, maxRingtoneVolume, 0)

        val maxMusicVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxMusicVolume, 0)

    }
}



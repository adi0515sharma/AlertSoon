package com.example.AlertSoon.ui.screens.home_screen_activity.ui.composable_screen.create_task_screen.ui

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.media.RingtoneManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.example.AlertSoon.R
import com.example.AlertSoon.ui.component.AppBar
import com.example.AlertSoon.ui.component.CollapsingLayout
import com.example.AlertSoon.ui.screens.home_screen_activity.ui.HomeActivity
import com.example.notifyme.ui.component.LoaderSection
import com.example.notifyme.ui.component.NoTaskAvailableUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SystemRingtoneScreen(navController: NavController, selected_uri: String? = null) {

    var music by rememberSaveable { mutableStateOf(mutableListOf<MusicClass>()) }
    var context = LocalContext.current
    var selectedUri by rememberSaveable { mutableStateOf(selected_uri) }
    var isLoaderVisible by rememberSaveable {
        mutableStateOf(true)
    }


    DisposableEffect(navController) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle back button press in Home composable
                // For example, navigate to another destination or finish the activity
                Log.e("AlertSoon", "selected_uri = ${selectedUri}")

                navController.previousBackStackEntry?.savedStateHandle?.set(
                    "selectedUri",
                    selectedUri
                )
                navController.popBackStack()
            }
        }
        (context as HomeActivity).onBackPressedDispatcher.addCallback(callback)
        onDispose {
            callback.remove()
        }
    }



    LaunchedEffect(key1 = null) {

        withContext(Dispatchers.IO) {
            music = listRingtones(context)
            isLoaderVisible = false
        }

    }
    Scaffold(
        topBar = {
            AppBar()
        },
        containerColor = MaterialTheme.colorScheme.background,
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(padding)
            ) {


                if (isLoaderVisible) {
                    LoaderSection()
                } else {
                    if(music.isEmpty()){
                        NoTaskAvailableUi()
                    }
                    else{
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {


                            itemsIndexed(music) { index, item ->


                                MusicViewComposble((item.name ?: item.uri), selectedUri == item.uri) {
                                    if (selectedUri == item.uri) {
                                        selectedUri = ""
                                        return@MusicViewComposble
                                    }
                                    selectedUri = item.uri
                                }

                            }
                        }
                    }

                }

            }
        },
    )

}


@Composable
fun MusicViewComposble(
    musicData: String,
    isSelected: Boolean,
    changeSelectedUri: () -> Unit
) {

    ConstraintLayout(
        modifier = Modifier
            .clickable {
                changeSelectedUri()
            }
            .padding(horizontal = 10.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
    ) {

        val title = createRef()
        val checkBox = createRef()

        Text(
            text = musicData,
            overflow = TextOverflow.Clip,
            maxLines = 1,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.constrainAs(title) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(checkBox.start)
                width = Dimension.fillToConstraints
            }
        )
        Checkbox(
            checked = isSelected,
            onCheckedChange = { changeSelectedUri() },
            modifier = Modifier.constrainAs(checkBox) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        )
    }
}

fun getMusicTitleFromUri(musicUri: String?, contentResolver: ContentResolver): String? {

    val projection = arrayOf(MediaStore.Audio.Media.DISPLAY_NAME)
    var cursor: Cursor? = null
    try {
        cursor = contentResolver.query(Uri.parse(musicUri), projection, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            val titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)
            return cursor.getString(titleColumn).lowercase(Locale.ROOT)
        }
    } catch (e: Exception) {
        Log.e("AlertSoon", "Error getting music title", e)
    } finally {
        cursor?.close()
    }
    Log.e("AlertSoon", "something went wrong")

    return null
}

fun listRingtones(context: Context?): MutableList<MusicClass> {
    var musicList = mutableListOf<MusicClass>()
    val ringtoneManager = RingtoneManager(context)
    ringtoneManager.setType(RingtoneManager.TYPE_RINGTONE)
    val cursor = ringtoneManager.cursor
    val count = cursor.count
    for (i in 0 until count) {
        val ringtoneUri = ringtoneManager.getRingtoneUri(i).toString()
        musicList.add(
            MusicClass(
                getMusicTitleFromUri(ringtoneUri, context?.contentResolver!!) ?: ringtoneUri,
                ringtoneUri
            )
        )
    }

    return musicList
}

data class MusicClass(val name: String?, val uri: String)
package com.alertSoon.alarm.ui.screens.home_screen_activity.ui.composable_screen.create_task_screen.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.DropdownMenu
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.emoji2.emojipicker.EmojiPickerView
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.alertSoon.alarm.R
import com.alertSoon.alarm.ui.utils.ApiResponse
import com.alertSoon.alarm.ui.utils.Validator.isFieldCorrect
import com.alertSoon.alarm.ui.utils.ValidatorResponse
import com.alertSoon.alarm.ui.component.AppBar
import com.alertSoon.alarm.ui.component.EntryFieldWithDialog
import com.alertSoon.alarm.ui.component.EntryFieldWithDialogForSelectingDays
import com.alertSoon.alarm.ui.component.AppButton
import com.alertSoon.alarm.ui.component.HandleClick
import com.alertSoon.alarm.ui.component.SelectedDateListener
import com.alertSoon.alarm.ui.component.SelectedTimeListener
import com.alertSoon.alarm.ui.component.SetEntryFieldValue
import com.alertSoon.alarm.ui.component.SetEntryFieldValueForDay
import com.alertSoon.alarm.ui.local_storage.Task.TableOfTask
import com.alertSoon.alarm.ui.navigation.FeatureNavScreen
import com.alertSoon.alarm.ui.theme.dimens
import com.alertSoon.alarm.ui.utils.Constants
import com.alertSoon.alarm.ui.utils.Constants.options
import com.alertSoon.alarm.ui.utils.DateTime
import com.alertSoon.alarm.ui.utils.DateTime.getNextTimeForRegularTask
import com.alertSoon.alarm.ui.utils.Validator.isDayFieldCorrect
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import java.net.URLEncoder
import java.util.Calendar
import java.util.Date


@Composable
fun CreateTaskComposable(
    navController: NavHostController,
    tableOfTask: TableOfTask,
    updateStateListener: ((TableOfTask) -> Unit),
    onInsertTask: (parameter: TableOfTask) -> Unit
) {

    Screen(navController, tableOfTask, updateStateListener , onInsertTask)

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen(
    navController: NavHostController,
    tableOfTask: TableOfTask,
    updateStateListener: ((TableOfTask) -> Unit),
    onInsertTask: (parameter: TableOfTask) -> Unit,

    ) {

    // Create a scroll state that remembers the scroll position
    val scrollState = rememberScrollState()

    val context = LocalContext.current

    var currentTaskState by rememberSaveable{mutableStateOf(tableOfTask)}



    var titleError by rememberSaveable { mutableStateOf<String?>(null) }
    var titleListener = object : SetEntryFieldValue {
        override fun setValue(s: String) {
            currentTaskState = currentTaskState.copy(task_title = s)
        }
    }

    var dateError by rememberSaveable { mutableStateOf<String?>(null) }
    var dateListener = object : SelectedDateListener {
        override fun setValue(day: Int, month: Int, year: Int) {

            Log.e("AlertSoon", "date = ${day}/${month}/${year}")
            val calendar : Calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)

            calendar.set(Calendar.HOUR_OF_DAY,0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND,0)

            currentTaskState = currentTaskState.copy(date_in_long = calendar.timeInMillis)
        }
    }

    var timeError by rememberSaveable { mutableStateOf<String?>(null) }
    var timeListener = object : SelectedTimeListener {
        override fun setValue(hour: Int, minute: Int) {

            Log.e("AlertSoon", "time = ${hour}:${minute}")

            val calendar : Calendar = Calendar.getInstance()

            calendar.set(Calendar.HOUR_OF_DAY,hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND,0)

            currentTaskState = currentTaskState.copy(time_in_long = calendar.timeInMillis)
        }
    }


    var descError by rememberSaveable { mutableStateOf<String?>(null) }
    var descListener = object : SetEntryFieldValue {
        override fun setValue(s: String) {
            currentTaskState = currentTaskState.copy(task_description = s)
        }
    }

    var showDialog by remember { mutableStateOf(false) }

    var anyToastMessage by rememberSaveable { mutableStateOf<String?>(null) }

    var expanded by rememberSaveable { mutableStateOf(false) }
    var shouldPlay by rememberSaveable { mutableStateOf(false) }

    var ringtoneState: Ringtone? by remember { mutableStateOf(null) }

    var dayOfTaskError by rememberSaveable { mutableStateOf<String?>(null) }
    var dayOfTaskListener = object : SetEntryFieldValueForDay {
        override fun setValue(value : String) {
            Log.e("AlertSoon", "regular days = ${value.toString()}")
            currentTaskState = currentTaskState.copy(days = value)
        }
    }

    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}






    DisposableEffect(Unit) {

        onDispose {
            ringtoneState?.stop()
            ringtoneState = null

            if (updateStateListener != null) {
                updateStateListener(currentTaskState)
            }

        }
    }

    LaunchedEffect(shouldPlay) {

        if(currentTaskState.sound != null){
            if(shouldPlay){
                ringtoneState = RingtoneManager.getRingtone(context, Uri.parse(currentTaskState.sound))
                ringtoneState?.play()
            }
            else{
                if(ringtoneState?.isPlaying == true){
                    ringtoneState?.stop()
                    ringtoneState = null
                }
            }
        }
    }

    LaunchedEffect(key1 = anyToastMessage) {
        if (anyToastMessage != null) {
            Toast.makeText(context, anyToastMessage, Toast.LENGTH_LONG).show()
            anyToastMessage = null
        }
    }


    LaunchedEffect(key1 = tableOfTask) {
        if (tableOfTask != null) {

            currentTaskState = currentTaskState.copy(
                uid = tableOfTask.uid,
                leadIcon = tableOfTask.leadIcon,
                task_title = tableOfTask.task_title,
                task_description = tableOfTask.task_description,
                time_in_long = tableOfTask.time_in_long,
                date_in_long = if(tableOfTask.is_regular) null else tableOfTask.date_in_long,
                sound = tableOfTask.sound,
                hour = tableOfTask.hour,
                minute = tableOfTask.minute,
                year = tableOfTask.year,
                month = tableOfTask.month,
                day_of_month = tableOfTask.day_of_month,
                is_regular = tableOfTask.is_regular,
                days = if(tableOfTask.is_regular) tableOfTask.days else "0000000",
                snozze_time = tableOfTask.snozze_time
            )

        }
    }
    // Content inside the scrollable column
    Scaffold(
        topBar = {
            AppBar()
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) {

        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            var (filled_section, btn_section) = createRefs()

            Column(
                modifier = Modifier
                    .padding(it)

                    .verticalScroll(state = scrollState)

                    .constrainAs(filled_section) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(btn_section.top)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.space_between_fields))

                    Text(
                        text = "Select Lead Icon",
                        fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                        fontSize = MaterialTheme.typography.labelLarge.fontSize,
                        fontWeight = MaterialTheme.typography.labelLarge.fontWeight
                    )
                    Text(
                        text = currentTaskState.leadIcon,
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            ),
                        ),
                                modifier = Modifier
                                    .clickable { showDialog = true }
                                    .padding(horizontal = MaterialTheme.dimens.select_icon_field_horizontal_padding, vertical = MaterialTheme.dimens.select_lead_icon_field_vertical_padding),
                        fontSize = MaterialTheme.typography.displayLarge.fontSize,
                    )
                    if (showDialog) {
                        Dialog(
                            onDismissRequest = { showDialog = false },
                            properties = DialogProperties(
                                dismissOnBackPress = true,
                                dismissOnClickOutside = true
                            ),
                        ) {
                            AndroidView(
                                modifier = Modifier
                                    .border(
                                        color = MaterialTheme.colorScheme.background,
                                        width = MaterialTheme.dimens.select_lead_icon_dialog_border_width,
                                        shape = RoundedCornerShape(MaterialTheme.dimens.select_lead_icon_dialog_rounded_corner_shape)
                                    )
                                    .fillMaxWidth()
                                    .height(MaterialTheme.dimens.select_lead_icon_dialog_height)
                                    .background(
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = RoundedCornerShape(MaterialTheme.dimens.select_lead_icon_dialog_bg_rounded_corner_shape)
                                    )
                                    .padding(MaterialTheme.dimens.select_lead_icon_dialog_all_padding),
                                factory = {
                                    EmojiPickerView(it)
                                        .apply {
                                            // setting row $columns - Optional
                                            emojiGridColumns = 6
                                            emojiGridRows = 6f

                                            // set pick listener
                                            setOnEmojiPickedListener { item ->

                                                currentTaskState = currentTaskState.copy(leadIcon = item.emoji)
                                                showDialog = false
                                            }
                                        }
                                }) {


                            }
                        }
                    }

                }

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.space_between_fields))
                EntryFieldWithDialog(
                    modifier = Modifier,
                    icon = R.drawable.baseline_title_24,
                    title = "Title",
                    hint = "Enter the title",
                    titleError,
                    currentTaskState.task_title,
                    titleListener
                )
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.space_between_fields))

                EntryFieldWithDialog(
                    modifier = Modifier,
                    title = "Description",
                    icon = R.drawable.baseline_description_24,
                    hint = "Enter the description",
                    error = descError,
                    value = currentTaskState.task_description,
                    setEntryFieldValue = descListener
                )
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.space_between_fields))

                Column(modifier = Modifier.fillMaxWidth()) {
                    EntryFieldWithDialog(
                        modifier = Modifier
                            .clickable {
                                expanded = !expanded
                            }
                            .onGloballyPositioned { coordinates ->

                                mTextFieldSize = coordinates.size.toSize()
                            },
                        title = "Type of alarm",
                        icon = R.drawable.baseline_alarm_24,
                        value = if(currentTaskState.is_regular) options[1] else options[0],
                        setEntryFieldValue = descListener,
                        shouldOpenDialog = false
                    )

                    DropdownMenu(
                        expanded = expanded,

                        onDismissRequest = { expanded = false },
                        modifier = Modifier.width(with(LocalDensity.current){mTextFieldSize.width.toDp()})
                    ) {
                        options.forEach { option ->
                            DropdownMenuItem(onClick = {
                                currentTaskState.is_regular = option == options[1]
                                expanded = false
                            }) {
                                Text(
                                    text = option,
                                    fontFamily = MaterialTheme.typography.labelMedium.fontFamily,
                                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                                    fontWeight = MaterialTheme.typography.labelMedium.fontWeight,
                                    modifier = Modifier.padding(vertical = MaterialTheme.dimens.checkbox_vertical_padding),
                                    style = TextStyle(
                                        platformStyle = PlatformTextStyle(
                                            includeFontPadding = false
                                        )
                                    )
                                )
                            }
                        }
                    }

                }


                Spacer(modifier = Modifier.height(MaterialTheme.dimens.space_between_fields))

                if(!currentTaskState.is_regular){
                    EntryFieldWithDialog(
                        modifier = Modifier.clickable { SelectDatePicker(context, dateListener) },
                        title = "Date",
                        icon = R.drawable.baseline_date_range_24,
                        hint = "Select your date",
                        error = dateError,
                        value = DateTime.getDateInFormat(currentTaskState.date_in_long),
                        shouldOpenDialog = false
                    )
                }
                else{
                    EntryFieldWithDialogForSelectingDays(
                        Modifier,
                        dayOfTaskError,
                        currentTaskState.days,
                        dayOfTaskListener
                    )
                }


                Spacer(modifier = Modifier.height(MaterialTheme.dimens.space_between_fields))
                EntryFieldWithDialog(
                    modifier = Modifier.clickable { SelectTimePicker(context, timeListener) },
                    title = "Time",
                    icon = R.drawable.baseline_access_time_24,
                    hint = "Select your time",
                    error = timeError,
                    value = DateTime.getTimeInFormat(currentTaskState.time_in_long),
                    shouldOpenDialog = false
                )

                if(currentTaskState.snozze_time!=null){
                    Spacer(modifier = Modifier.height(MaterialTheme.dimens.space_between_fields))
                    ConstraintLayout(modifier = Modifier.fillMaxWidth()){

                        val fields = createRef()
                        val delete = createRef()

                        EntryFieldWithDialog(
                            modifier = Modifier.constrainAs(fields){
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                                end.linkTo(delete.start)
                                width = Dimension.fillToConstraints
                            },
                            title = "Snoozed",
                            icon = R.drawable.baseline_snooze_24,
                            hint = null,
                            error = null,
                            value = DateTime.getTimeTextForSnooze(currentTaskState.snozze_time!!),
                            shouldOpenDialog = false
                        )

                        Image(
                            painter = painterResource(id = R.drawable.baseline_delete_24),
                            contentDescription = null,
                            modifier = Modifier
                                .constrainAs(delete) {
                                    top.linkTo(parent.top)
                                    start.linkTo(fields.end)
                                    bottom.linkTo(parent.bottom)
                                    end.linkTo(parent.end)
                                }
                                .padding(end = 13.dp)
                                .size(MaterialTheme.dimens.card_task_ui_delete_size)
                                .clickable {
                                    currentTaskState = currentTaskState.copy(snozze_time = null)
                                }
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                )
                                .padding(MaterialTheme.dimens.card_task_ui_delete_padding),
                            colorFilter = ColorFilter.tint(color = Color.White)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(MaterialTheme.dimens.space_between_fields))

                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {

                    val fields = createRef()
                    val play_pause = createRef()
                    EntryFieldWithDialog(
                        modifier = Modifier
                            .constrainAs(fields) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                                end.linkTo(play_pause.start)
                                width = Dimension.fillToConstraints
                            }
                            .clickable {

                                shouldPlay = false
                                if (currentTaskState.sound.isNullOrEmpty()) {
                                    navController.navigate(
                                        FeatureNavScreen.SELECT_RINGTONE.name.replace(
                                            "{selectedUri}",
                                            ""
                                        )
                                    )
                                } else {
                                    navController.navigate(
                                        FeatureNavScreen.SELECT_RINGTONE.name.replace(
                                            "{selectedUri}",
                                            URLEncoder.encode(currentTaskState.sound, "UTF-8")
                                        )
                                    )
                                }


                            },
                        title = "Sound",
                        icon = R.drawable.baseline_music_note_24,
                        hint = "Select your sound",
                        error = null,
                        value = getMusicTitleFromUri(currentTaskState.sound, context.contentResolver)?:currentTaskState.sound,
                        shouldOpenDialog = false
                    )

                    if(currentTaskState.sound!=null && currentTaskState.sound?.isNotEmpty() == true){
                        Image(
                            painter = painterResource(id = if(shouldPlay) R.drawable.baseline_pause_24 else R.drawable.baseline_play_arrow_24),
                            contentDescription = null,
                            modifier = Modifier
                                .constrainAs(play_pause) {
                                    top.linkTo(parent.top)
                                    start.linkTo(fields.end)
                                    bottom.linkTo(parent.bottom)
                                    end.linkTo(parent.end)
                                }
                                .padding(end = 13.dp)
                                .size(MaterialTheme.dimens.card_task_ui_delete_size)
                                .clickable {
                                    shouldPlay = !shouldPlay
                                }
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                )
                                .padding(MaterialTheme.dimens.card_task_ui_delete_padding),
                            colorFilter = ColorFilter.tint(color = Color.White)
                        )
                    }

                }


            }


            Row(modifier = Modifier
                .padding(MaterialTheme.dimens.create_task_section_action_button_padding)
                .fillMaxWidth()
                .constrainAs(btn_section) {
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }) {
                Box(modifier = Modifier.weight(0.5f)) {

                    AppButton(Modifier.fillMaxWidth(),"Cancel", object : HandleClick {
                        override fun onClick() {
                            navController.navigateUp()
                        }
                    })
                }
                Spacer(modifier = Modifier.width(MaterialTheme.dimens.create_task_section_action_button_spacer))
                Box(modifier = Modifier.weight(0.5f)) {

                    AppButton(Modifier.fillMaxWidth(),"Save", object : HandleClick {
                        override fun onClick() {
                            val titleVarify = currentTaskState.task_title.isFieldCorrect("title")
                            if (titleVarify != ValidatorResponse.Success) {
                                titleError =
                                    (titleVarify as ValidatorResponse.Error).message
                                return
                            } else {
                                titleError = null
                            }


                            val descriptionVarify = currentTaskState.task_description.isFieldCorrect("description")
                            if (descriptionVarify != ValidatorResponse.Success) {
                                descError =
                                    (descriptionVarify as ValidatorResponse.Error).message
                                return
                            } else {
                                descError = null
                            }

                            if(!currentTaskState.is_regular){
                                val dateVarify = currentTaskState.date_in_long.isFieldCorrect("date")
                                if (dateVarify != ValidatorResponse.Success) {
                                    dateError = (dateVarify as ValidatorResponse.Error).message
                                    return
                                } else {
                                    dateError = null
                                }
                            }



                            val timeVarify = currentTaskState.time_in_long.isFieldCorrect("time")
                            if (timeVarify != ValidatorResponse.Success) {
                                timeError = (timeVarify as ValidatorResponse.Error).message
                                return
                            } else {
                                timeError = null
                            }

                            if(currentTaskState.is_regular){
                                val dayVarify = currentTaskState.days.isDayFieldCorrect()
                                if (dayVarify != ValidatorResponse.Success) {
                                    dayOfTaskError =
                                        (dayVarify as ValidatorResponse.Error).message
                                    return
                                } else {
                                    dayOfTaskError = null
                                }
                            }



                            if (currentTaskState.is_regular)
                            {
                                if(currentTaskState.snozze_time !=null){

                                    if(currentTaskState.snozze_time!! <= DateTime.getTime()) {
                                        anyToastMessage = "Snoozed time is not valid please check again"
                                        return
                                    }
                                }
                                currentTaskState = currentTaskState.copy(time_in_long = currentTaskState.getNextTimeForRegularTask())
                            }
                            else
                            {

                                val calender_time = Calendar.getInstance()
                                calender_time.timeInMillis = currentTaskState.time_in_long!!
                                val calender_date = Calendar.getInstance()
                                calender_date.timeInMillis = currentTaskState.date_in_long!!
                                Log.e("AlertSoon", "once task time = ${calender_time.timeInMillis} , ${currentTaskState.time_in_long!!}")
                                Log.e("AlertSoon", "once task date = ${calender_date.timeInMillis} , ${currentTaskState.date_in_long!!}")

                                val final_time_calendar = Calendar.getInstance()
                                final_time_calendar.set(Calendar.YEAR,calender_date.get(Calendar.YEAR))
                                final_time_calendar.set(Calendar.MONTH,calender_date.get(Calendar.MONTH))
                                final_time_calendar.set(Calendar.DAY_OF_MONTH,calender_date.get(Calendar.DAY_OF_MONTH))
                                final_time_calendar.set(Calendar.HOUR_OF_DAY,calender_time.get(Calendar.HOUR_OF_DAY))
                                final_time_calendar.set(Calendar.MINUTE,calender_time.get(Calendar.MINUTE))
                                final_time_calendar.set(Calendar.SECOND,0)
                                final_time_calendar.set(Calendar.MILLISECOND,0)

                                if(currentTaskState.snozze_time !=null){

                                    if(currentTaskState.snozze_time!! <= DateTime.getTime()) {
                                        anyToastMessage = "Snoozed time is not valid please check again"
                                        return
                                    }
                                }
                                else if(final_time_calendar.timeInMillis <= DateTime.getTime()) {
                                    anyToastMessage = "Selected date or time is not valid please check again"
                                    return
                                }

                                currentTaskState = currentTaskState.copy(time_in_long = final_time_calendar.timeInMillis)
                            }

                            onInsertTask(currentTaskState)

                        }
                    })
                }
            }
        }

    }

}


@Preview(showSystemUi = true)
@Composable
fun view() {
    var calendarDate by rememberSaveable { mutableStateOf(Calendar.getInstance()) }
    var navController: NavHostController? = null
//    Screen(
//        navController = navController!!,
//        tableOfTask =  TableOfTask(12, task_title = "Hello world", task_description = "Hello world", time_in_long = 168932000, date_in_long = 168930000, sound=""),
//        calendarDate = calendarDate,
//        onInsertTask =  {}
//    )
}

fun SelectDatePicker(mContext: Context, dateFieldValue: SelectedDateListener?) {
    // Fetching the Local Context

    // Declaring integer values
    // for year, month and day
    val mYear: Int
    val mMonth: Int
    val mDay: Int

    // Initializing a Calendar
    val mCalendar = Calendar.getInstance()

    // Fetching current year, month and day
    mYear = mCalendar.get(Calendar.YEAR)
    mMonth = mCalendar.get(Calendar.MONTH)
    mDay = mCalendar.get(Calendar.DAY_OF_MONTH)

    mCalendar.time = Date()

    // Declaring a string value to
    // store date in string format

    // Declaring DatePickerDialog and setting
    // initial values as current values (present year, month and day)
    val mDatePickerDialog = DatePickerDialog(
        mContext,
        { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->

            dateFieldValue?.setValue(mDayOfMonth, mMonth, mYear)
        }, mYear, mMonth, mDay
    )

    mDatePickerDialog.show()

}
fun SelectTimePicker(mContext: Context, timeFieldValue: SelectedTimeListener?) {

    // Fetching local context

    // Declaring and initializing a calendar
    val mCalendar = Calendar.getInstance()
    val mHour = mCalendar[Calendar.HOUR_OF_DAY]
    val mMinute = mCalendar[Calendar.MINUTE]

    // Value for storing time as a string

    // Creating a TimePicker dialod
    val mTimePickerDialog = TimePickerDialog(
        mContext,
        { _, mHour: Int, mMinute: Int ->

            timeFieldValue?.setValue(mHour, mMinute)

        }, mHour, mMinute, true
    )

    mTimePickerDialog.show()

}
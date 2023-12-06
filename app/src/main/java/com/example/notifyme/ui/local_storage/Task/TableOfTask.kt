package com.example.AlertSoon.ui.local_storage.Task

import android.os.Parcelable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Task")
data class TableOfTask(

    @PrimaryKey(autoGenerate = true)
    val uid: Long? = null,

    @ColumnInfo("lead_icon")
    val leadIcon: String = "🎯",

    @ColumnInfo("title")
    val task_title: String?=null,

    @ColumnInfo("description")
    val task_description: String?=null,

    @ColumnInfo("time_in_long")
    var time_in_long: Long?=null,

    @ColumnInfo("hour")
    val hour: Int?=null,

    @ColumnInfo("minute")
    val minute: Int?=null,

    @ColumnInfo("date_in_long")
    val date_in_long: Long? = null,

    @ColumnInfo("year")
    val year: Int?=null,

    @ColumnInfo("month")
    val month: Int?=null,

    @ColumnInfo("day_of_month")
    val day_of_month: Int?=null,

    @ColumnInfo("sound")
    var sound: String?=null,

    @ColumnInfo("is_regular")
    var is_regular: Boolean = false,

    @ColumnInfo("days")
    var days: String = "0000000",

    @ColumnInfo("snozze_time")
    var snozze_time: Long? = null,
    )  : Parcelable




//object TableOfTaskSaver : Saver<TableOfTask, Map<String, Any?>> {
//    override fun restore(value: Map<String, Any?>): TableOfTask {
//        val uid = value["uid"] as Long?
//        val leadIcon = value["leadIcon"] as String? ?: "🎯"
//        val task_title = value["task_title"] as String
//        val task_description = value["task_description"] as String
//        val time_in_long = value["time_in_long"] as Long
//        val hour = value["hour"] as Int
//        val minute = value["minute"] as Int
//        val date_in_long = value["date_in_long"] as Long?
//        val year = value["year"] as Int
//        val month = value["month"] as Int
//        val day_of_month = value["day_of_month"] as Int
//        val sound = value["sound"] as String?
//        val is_regular = value["is_regular"] as Boolean
//        val days = value["days"] as String
//        val snozze_time = value["snozze_time"] as Long
//
//        return TableOfTask(
//            uid,
//            leadIcon,
//            task_title,
//            task_description,
//            time_in_long,
//            hour,
//            minute,
//            date_in_long,
//            year,
//            month,
//            day_of_month,
//            sound,
//            is_regular,
//            days,
//            snozze_time
//        )
//    }
//
//    override fun SaverScope.save(tableOfTask: TableOfTask) = mapOf(
//        "uid" to tableOfTask.uid,
//        "leadIcon" to tableOfTask.leadIcon,
//        "task_title" to tableOfTask.task_title,
//        "task_description" to tableOfTask.task_description,
//        "time_in_long" to tableOfTask.time_in_long,
//        "hour" to tableOfTask.hour,
//        "minute" to tableOfTask.minute,
//        "date_in_long" to tableOfTask.date_in_long,
//        "year" to tableOfTask.year,
//        "month" to tableOfTask.month,
//        "day_of_month" to tableOfTask.day_of_month,
//        "sound" to tableOfTask.sound,
//        "is_regular" to tableOfTask.is_regular,
//        "days" to tableOfTask.days,
//        "snozze_time" to tableOfTask.snozze_time
//    )
//}


//
//fun toCombinedTaskModelToTableOfTaskForInsert(it : CombinedTaskModel) : TableOfTask{
//
//    val time = it.time_in_long!!
//    val date = it.date_in_long!!
//
//    return TableOfTask(
//        uid = it.uid,
//        leadIcon = it.leadIcon,
//        task_title = it.task_title!!,
//        task_description = it.task_description!!,
//        time_in_long = time,
//        date_in_long = date,
//        sound = it.sound,
//        is_regular = false,
//    )
//}
//
//
//fun toTableOfRegularTaskToTableOfTask(it : TableOfRegularTask) : TableOfTask{
//    val calendarForTime = Calendar.getInstance()
//    calendarForTime.timeInMillis = it.time_in_long
//
//
//    val calendar = Calendar.getInstance()
//    calendar.set(Calendar.HOUR_OF_DAY, calendarForTime.get(Calendar.HOUR_OF_DAY))
//    calendar.set(Calendar.MINUTE, calendarForTime.get(Calendar.MINUTE))
//    calendar.set(Calendar.SECOND, 0)
//    calendar.set(Calendar.MILLISECOND, 0)
//
//    val time = calendar.timeInMillis
//    val date = DateTime.getTodayDate()
//    val id = it.uid
//
//    return TableOfTask(
//        leadIcon = it.leadIcon,
//        task_title = it.task_title,
//        task_description = it.task_description,
//        time_in_long = time,
//        date_in_long = date,
//        sound = it.sound,
//        is_regular = true,
//        id_regular = id
//    )
//}

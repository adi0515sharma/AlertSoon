package com.alertSoon.alarm.ui.local_storage.Task

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


    @ColumnInfo("date_in_long")
    val date_in_long: Long? = null,


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




package com.example.AlertSoon.ui.screens.home_screen_activity.ui.composable_screen.create_task_screen

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.emoji2.emojipicker.EmojiViewItem
import com.example.AlertSoon.ui.local_storage.Task.TableOfTask
//
//data class CombinedTaskModel(
//    var uid: Long? = null,
//    var leadIcon: String = "🎯",
//    var task_title: String? = null,
//    var task_description: String? = null,
//    var time_in_long: Long? = null,
//    var date_in_long: Long? = null,
//    var term: String = Term.FUTURE.name,
//    var sound: String? = null,
//    var is_regular: Boolean = false,
//    var id_regular: Long? = null,
//
//
//    //below extra part of regular task
//    var hour_in_day: Int? = null,
//    var minute_in_day: Int? = null,
//    var am_or_pm: String? = null,
//
//    //not part of table
//    var date: String? = null,
//    var time: String? = null,
//    var emojiViewItem: EmojiViewItem? = EmojiViewItem("🎯", emptyList()),
//    var dayOfTask : MutableList<Boolean> = mutableListOf(false, false, false, false, false, false, false),
//
//    var beforeItWas : String? = null
//
//    )
//
//
//object CombinedTaskModelSaver : Saver<CombinedTaskModel, Map<String, Any?>> {
//    override fun restore(value: Map<String, Any?>): CombinedTaskModel? {
//        val uid = value["uid"] as Long
//        val leadIcon = value["leadIcon"] as String? ?: "🎯"
//        val task_title = value["task_title"] as String?
//        val task_description = value["task_description"] as String?
//        val time_in_long = value["time_in_long"] as Long?
//        val date_in_long = value["date_in_long"] as Long?
//        val term = value["term"] as String? ?: Term.FUTURE.name
//        val sound = value["sound"] as String?
//        val is_regular = value["is_regular"] as Boolean? ?: false
//        val id_regular = value["id_regular"] as Long?
//        val hour_in_day = value["hour_in_day"] as Int?
//        val minute_in_day = value["minute_in_day"] as Int?
//        val am_or_pm = value["am_or_pm"] as String?
//        val date = value["date"] as String?
//        val time = value["time"] as String?
//        val emojiViewItem = value["emojiViewItem"] as EmojiViewItem?
//        val dayOfTask = value["dayOfTask"] as MutableList<Boolean>
//
//        val combinedTaskModel = CombinedTaskModel(
//            uid,
//            leadIcon,
//            task_title,
//            task_description,
//            time_in_long,
//            date_in_long,
//            term,
//            sound,
//            is_regular,
//            id_regular,
//            hour_in_day,
//            minute_in_day,
//            am_or_pm,
//            date,
//            time,
//            emojiViewItem,
//            dayOfTask
//        )
//
//        return combinedTaskModel
//    }
//
//    override fun SaverScope.save(combinedTaskModel: CombinedTaskModel): Map<String, Any?> {
//        val taskModelMap = mapOf(
//            "uid" to combinedTaskModel.uid as Any?,
//            "leadIcon" to combinedTaskModel.leadIcon as Any,
//            "task_title" to combinedTaskModel.task_title as Any?,
//            "task_description" to combinedTaskModel.task_description as Any?,
//            "time_in_long" to combinedTaskModel.time_in_long as Any?,
//            "date_in_long" to combinedTaskModel.date_in_long as Any?,
//            "term" to combinedTaskModel.term as Any,
//            "sound" to combinedTaskModel.sound as Any?,
//            "is_regular" to combinedTaskModel.is_regular as Any,
//            "id_regular" to combinedTaskModel.id_regular as Any?,
//            "hour_in_day" to combinedTaskModel.hour_in_day as Any?,
//            "minute_in_day" to combinedTaskModel.minute_in_day as Any?,
//            "am_or_pm" to combinedTaskModel.am_or_pm as Any?,
//            "date" to combinedTaskModel.date as Any?,
//            "time" to combinedTaskModel.time as Any?,
//            "emojiViewItem" to combinedTaskModel.emojiViewItem as Any?,
//            "dayOfTask" to combinedTaskModel.dayOfTask as Any
//        )
//        return taskModelMap
//    }
//
//}
//
//
//fun toTableOfTaskToCombinedTaskModel(it : TableOfTask) : CombinedTaskModel {
//
//    val time = it.time_in_long
//    val date = it.date_in_long
//
//    return CombinedTaskModel(
//        uid = it.uid,
//        leadIcon = it.leadIcon,
//        task_title = it.task_title,
//        task_description = it.task_description,
//        time_in_long = time,
//        date_in_long = date,
//        sound = it.sound,
//        is_regular = false,
//    )
//}
//

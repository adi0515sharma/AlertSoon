package com.alertSoon.alarm.ui.local_storage.Task

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.alertSoon.alarm.ui.utils.DateTime
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

@Dao
interface TaskDao {

    @Insert
    suspend fun insertTask(tasks : TableOfTask) : Long

    @Query("SELECT * FROM Task")
    suspend fun getAllTasks() : List<TableOfTask>

    @Query("DELETE FROM Task WHERE uid = :id")
    suspend fun deleteTaskById(id : Long)

    @Query("SELECT * FROM Task WHERE uid = :id")
    suspend fun getTaskById(id : Long) : TableOfTask?

    @Update
    suspend fun updateTask(tableOfTask : TableOfTask)

    @Query("UPDATE Task SET snozze_time = :snooze_time WHERE uid = :id")
    suspend fun snoozeTask(id : Long, snooze_time : Long?)

    @Query("SELECT * FROM Task ORDER BY CASE WHEN snozze_time IS NOT NULL THEN snozze_time ELSE time_in_long END ASC LIMIT 5")
    fun getNextFiveTask() : Flow<MutableList<TableOfTask>>

    @Query("SELECT * FROM Task WHERE is_regular = :is_regular ORDER BY CASE WHEN snozze_time IS NOT NULL THEN snozze_time ELSE time_in_long END")
    fun getOnceTasks(is_regular : Boolean = false) : Flow<MutableList<TableOfTask>>

    @Query("SELECT * FROM Task WHERE is_regular = :is_regular ORDER BY time_in_long ASC")
    fun getRegularTasks(is_regular : Boolean = true) : Flow<MutableList<TableOfTask>>

    @Query("SELECT * FROM Task WHERE is_regular = :is_regular")
    fun getRegularTaskForReschedule(is_regular : Boolean = true) : MutableList<TableOfTask>

}
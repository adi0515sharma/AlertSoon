package com.alertSoon.alarm.ui.utils

import com.alertSoon.alarm.ui.local_storage.Task.TableOfTask
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


object DateTime{
    fun getAmPm(timestamp: Long): String {
        // Convert the timestamp to a Date object
        val date = Date(timestamp)

        // Create a SimpleDateFormat object to format the time as AM or PM
        val amPmFormat = SimpleDateFormat("a")

        // Format the date and time as AM/PM
        val amPm = amPmFormat.format(date)

        return amPm
    }

    fun getTime(timestamp: Long): String {
        // Convert the timestamp to a Date object
        val date = Date(timestamp)

        // Create a SimpleDateFormat object to format the time in 12-hour format without AM/PM
        val sdf = SimpleDateFormat("hh:mm")

        // Format the date and time
        val formattedTime = sdf.format(date)


        return formattedTime
    }




    fun getTime() : Long {
        val calendarDate: Calendar = Calendar.getInstance()
        calendarDate.set(Calendar.SECOND,0)
        calendarDate.set(Calendar.MILLISECOND,0)
        return calendarDate.timeInMillis
    }

    fun getDateInFormat(date : Long?) = if(date!=null) SimpleDateFormat("dd MMMM, yyyy", Locale.US).format(Date(date)) else null



    fun getTimeInFormat(calendar : Calendar) = SimpleDateFormat("hh:mm a", Locale.US).format(Date(calendar.timeInMillis))
    fun getTimeInFormat(time : Long?) = if(time!=null) SimpleDateFormat("hh:mm a", Locale.US).format(Date(time)) else null



    fun getDateByIterate(date: Long): String {
        if (date == getTodayDate()) {
            return "Today"
        }
        if (date == getTomorrowDate()) {
            return "Tomorrow"
        }

        return SimpleDateFormat("dd MMMM, yyyy", Locale.US).format(Date(date))
    }

    fun getTomorrowDate(): Long {
        val calendarDate: Calendar = Calendar.getInstance()
        calendarDate.add(Calendar.DAY_OF_YEAR, 1) // Add one day to the current date
        calendarDate.set(Calendar.HOUR_OF_DAY, 0)
        calendarDate.set(Calendar.MINUTE, 0)
        calendarDate.set(Calendar.SECOND, 0)
        calendarDate.set(Calendar.MILLISECOND, 0)
        return calendarDate.timeInMillis
    }

    fun getTodayDate(): Long {
        val calendarDate: Calendar = Calendar.getInstance()
        calendarDate.set(Calendar.HOUR_OF_DAY, 0)
        calendarDate.set(Calendar.MINUTE, 0)
        calendarDate.set(Calendar.SECOND, 0)
        calendarDate.set(Calendar.MILLISECOND, 0)
        return calendarDate.timeInMillis
    }

    fun getTimeTextForSnooze(timeInMills : Long) : String{

        val timeInString = getTimeInFormat(timeInMills)

        val calendarOfSelectedTime = Calendar.getInstance()
        calendarOfSelectedTime.timeInMillis = timeInMills

        val calendarTodayDate = Calendar.getInstance()
        val calendarTomorowDate = Calendar.getInstance()
        calendarTomorowDate.add(Calendar.DAY_OF_YEAR, 1);

        if(calendarTodayDate.get(Calendar.YEAR) == calendarOfSelectedTime.get(Calendar.YEAR) && calendarTodayDate.get(Calendar.MONTH) == calendarOfSelectedTime.get(Calendar.MONTH) && calendarTodayDate.get(Calendar.DAY_OF_MONTH) == calendarOfSelectedTime.get(Calendar.DAY_OF_MONTH)){
            return "Today at $timeInString"
        }
        else if(calendarTomorowDate.get(Calendar.YEAR) == calendarOfSelectedTime.get(Calendar.YEAR) && calendarTomorowDate.get(Calendar.MONTH) == calendarOfSelectedTime.get(Calendar.MONTH) && calendarTomorowDate.get(Calendar.DAY_OF_MONTH) == calendarOfSelectedTime.get(Calendar.DAY_OF_MONTH)){
            return "Tomorrow at $timeInString"
        }

        return "${getDateInFormat(calendarOfSelectedTime.timeInMillis)} at $timeInString"
    }

    fun isPast(compare_withable_time: Long?): Boolean{

        var calendarTime = Calendar.getInstance()

        val taskCalendarTime = Calendar.getInstance()
        taskCalendarTime.timeInMillis = compare_withable_time!!

        calendarTime.set(Calendar.HOUR_OF_DAY, taskCalendarTime.get(Calendar.HOUR_OF_DAY))
        calendarTime.set(Calendar.MINUTE, taskCalendarTime.get(Calendar.MINUTE))
        calendarTime.set(Calendar.MILLISECOND,0)
        calendarTime.set(Calendar.SECOND,0)
        return isExperied(calendarTime.timeInMillis)
    }


    fun TableOfTask.getNextTimeForRegularTask(newTask : Boolean = false) : Long{

        if(!(days.contains('1'))){
            return 0L
        }


        val index = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1

        val step = if(newTask && days[index] == '1' && !isPast(time_in_long)){
            0
        }
        else{
            val findOneAtRightSide = days.indexOf('1',index+1)
            val jumps = if(findOneAtRightSide != -1){
                findOneAtRightSide - index
            } else{
                days.length - index +  days.indexOf('1')
            }
            jumps
        }



        val taskCalendarTime = Calendar.getInstance()
        taskCalendarTime.timeInMillis = time_in_long!!

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, step)

        calendar.set(Calendar.HOUR_OF_DAY, taskCalendarTime.get(Calendar.HOUR_OF_DAY))
        calendar.set(Calendar.MINUTE, taskCalendarTime.get(Calendar.MINUTE))
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }


    fun isExperied(time_in_long : Long?) : Boolean{
        return getTime() >= time_in_long!!
    }
}
package com.example.AlertSoon.ui.utils

import com.example.AlertSoon.ui.local_storage.Task.TableOfTask
import java.text.SimpleDateFormat
import java.time.LocalDate
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

    fun getDate() : Long {
        val calendarDate: Calendar = Calendar.getInstance()
        calendarDate.set(Calendar.HOUR_OF_DAY, 0)
        calendarDate.set(Calendar.MINUTE, 0)
        calendarDate.set(Calendar.SECOND, 0)
        calendarDate.set(Calendar.MILLISECOND, 0)
        return calendarDate.timeInMillis
    }


    fun getTime() : Long {
        val calendarDate: Calendar = Calendar.getInstance()
        calendarDate.set(Calendar.SECOND,0)
        calendarDate.set(Calendar.MILLISECOND,0)
        return calendarDate.timeInMillis
    }

    fun getTimePlusMinutes() : Long {
        val calendarDate: Calendar = Calendar.getInstance()
        calendarDate.set(Calendar.SECOND,0)
        calendarDate.set(Calendar.MILLISECOND,0)

        calendarDate.add(Calendar.MINUTE,15)
        return calendarDate.timeInMillis
    }
    fun getDateInFormat(calendar : Calendar) = SimpleDateFormat("dd MMMM, yyyy", Locale.US).format(Date(calendar.timeInMillis))
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

    fun getOnlyDate(current : Long) : Long{
        val calendar : Calendar = Calendar.getInstance()
        calendar.timeInMillis = current

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.timeInMillis
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

    fun getTodaysDay() : String{
        val calendar = Calendar.getInstance()
        val currentDate = calendar.time
        val sdf = SimpleDateFormat("EEEE")
        val dayOfWeek = sdf.format(currentDate)
        return dayOfWeek.lowercase()
    }


    fun TableOfTask.getNextTimeForRegularTask() : Long{
        if(!(this.days.contains('1'))){
            return 0L
        }

        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1

        var step = 0
        var index = dayOfWeek

        var result: Long;
        while (true){
            if(this.days[index] == '1'){

                val calendar_time = Calendar.getInstance()
                calendar_time.timeInMillis = this.time_in_long!!

                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_YEAR, step)

                calendar.set(Calendar.HOUR_OF_DAY, calendar_time.get(Calendar.HOUR_OF_DAY))
                calendar.set(Calendar.MINUTE, calendar_time.get(Calendar.MINUTE))
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                if(calendar.timeInMillis > getTime()){
                    result = calendar.timeInMillis
                    break
                }
            }
            if(index == this.days.length-1){
                index = 0
            }
            else{
                index++
            }
            step++
        }

        return result
    }


    fun isThisTaskShouldScheduleForToday(days : String) : Boolean{
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1
        return days[dayOfWeek] == '1'
    }
}
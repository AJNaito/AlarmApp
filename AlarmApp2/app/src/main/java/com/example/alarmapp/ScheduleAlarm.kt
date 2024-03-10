package com.example.alarmapp

import android.content.Context
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import java.util.*

object ScheduleAlarm {
    fun scheduleAlarm (context : Context, alarmInfo : AlarmInfo) {
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager

        val timeZones = context.resources.getStringArray(R.array.TimeZones)

        val timeZone = TimeZone.getTimeZone(timeZones[alarmInfo.TimeZone])
        val calendar = Calendar.getInstance(timeZone)

        calendar.set(Calendar.HOUR_OF_DAY, alarmInfo.Hour)
        calendar.set(Calendar.MINUTE, alarmInfo.Min)

        val info = alarmInfo.Days.split(",")
        for (i in info) {
            if (i == "Mon")
                calendar.add(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            if (i == "Tue")
                calendar.add(Calendar.DAY_OF_WEEK, Calendar.TUESDAY)
            if (i == "Wed")
                calendar.add(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY)
            if (i == "Thu")
                calendar.add(Calendar.DAY_OF_WEEK, Calendar.THURSDAY)
            if (i == "Fri")
                calendar.add(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
            if (i == "Sat")
                calendar.add(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
            if (i == "Sun")
                calendar.add(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        }

        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, alarmInfo.alarmID, intent, PendingIntent.FLAG_IMMUTABLE)

        val time = (calendar.timeInMillis)

        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    fun removeAlarm(context: Context, id : Int) {
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, id, intent,
            PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
    }
}
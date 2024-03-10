package com.example.alarmapp

import android.content.Context
import java.io.File
import java.util.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import org.json.JSONArray
import java.io.FileOutputStream

object ReadWriteJson {
    fun ReadAlarms(context: Context) : ArrayList<AlarmInfo> {
        val root = context.getExternalFilesDir(null)
        val dir = "$root/Alarms/Alarm.txt"

        val alarms : ArrayList<AlarmInfo> = arrayListOf()
        try {
            // read the file
            val input = context.openFileInput(dir)
            val size = input.available()
            val buf = ByteArray(size)
            input.read(buf)
            input.close()

            // Read JSON
            val json = JSONArray(buf)
            for (i in 0..json.length()) {
                alarms.add(json.get(i) as AlarmInfo)
            }
        } catch (_: Exception) {
        }

        return alarms
    }

    fun WriteAlarm(context : Context, alarm : AlarmInfo) : Boolean {
        val root = context.getExternalFilesDir(null)
        val dir = File("$root/Alarms")
        dir.mkdirs()

        val string = Json.encodeToString(alarm)

        val file = File(dir, "Alarm.txt")
        try {
            if (file.exists()) {
                file.appendText(string)
            } else {
                file.writeText(string)
            }
        } catch (_ : Exception) {
            return false
        }

        return true
    }

    fun RewriteAlarm(context : Context, alarms : ArrayList<AlarmInfo>) : Boolean {
        val root = context.getExternalFilesDir(null)
        val dir = File("$root/Alarms")
        dir.mkdirs()

        val string = Json.encodeToString(alarms)

        val file = File(dir, "Alarm.txt")
        if (file.exists()) file.delete()

        try {
            file.writeText(string)
        } catch (_ : Exception) {
            return false
        }

        return true
    }
}
package com.example.alarmapp

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "alarm_table")
data class AlarmTable (
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey(autoGenerate = true)
    var alarmID : Int,
    @field:ColumnInfo(name = "hour")
    var hour : Int,
    @field:ColumnInfo(name = "minute")
    var min : Int,
    @field:ColumnInfo(name = "ampm")
    var ampm : Boolean,
    @field:ColumnInfo(name = "activated")
    var active : Boolean,
    @field:ColumnInfo(name = "timezone")
    var timeZone : Int,
    @field:ColumnInfo(name = "days")
    var days : String
)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AlarmTable
        return alarmID == other.alarmID
    }

    override fun hashCode(): Int {
        var result = alarmID
        result = 31 * result + hour
        result = 31 * result + min
        result = 31 * result + ampm.hashCode()
        result = 31 * result + active.hashCode()
        result = 31 * result + timeZone
        result = 31 * result + days.hashCode()
        return result
    }
}
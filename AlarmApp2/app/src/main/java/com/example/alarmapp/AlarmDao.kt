package com.example.alarmapp

import androidx.room.*
import kotlinx.coroutines.flow.Flow
////ar Hour : Int, var Min : Int, var AMPM : Boolean, var Activated : Boolean, var TimeZone : Int, var Days : String
@Dao
interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alarmTable : AlarmTable)

    @Query("DELETE FROM alarm_table WHERE id == :alarmID")
    suspend fun deleteAlarm(alarmID : Int)

    @Query("UPDATE alarm_table SET hour = :hour, minute = :min, ampm = :ampm, activated = :active, timeZone = :timeZone, days = :days WHERE id == :alarmID")
    suspend fun updateAlarm(alarmID: Int, hour : Int, min : Int, ampm : Boolean, active : Boolean, timeZone : Int, days : String)

    @Query("Delete FROM alarm_table")
    suspend fun deleteAll()

    @Query("Select * FROM alarm_table where id == :alarmID")
    suspend fun getAlarm(alarmID: Int) : AlarmTable?

    @Query("Select * FROM alarm_table")
    fun getAllAlarm() : List<AlarmTable>?
}
package com.example.alarmapp

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//ar Hour : Int, var Min : Int, var AMPM : Boolean, var Activated : Boolean, var TimeZone : Int, var Days : String

class AlarmRepo private constructor (alarmDao: AlarmDao){
    // temp data
    val data = MutableLiveData<AlarmInfo>()

    // list data
    val allAlarms = MutableLiveData<ArrayList<AlarmInfo>>()

    private var hour: Int = 0
    private var min : Int = 0
    private var ampm : Boolean = true
    private var active : Boolean = false
    private var timeZone : Int = 0
    private var days : String = ""

    var id : Int = -1

    private var mAlarmDao:AlarmDao = alarmDao

    fun clearData() {
        hour = 0
        min = 0
        ampm = true
        active = false
        timeZone = 0
        days  = ""
        id = -1
    }

    fun deleteAlarm(alarmID : Int, position : Int) {
        mScope.launch(Dispatchers.IO) {
            mAlarmDao.deleteAlarm(alarmID)
            val alarms = allAlarms.value
            if (alarms != null) {
                alarms.removeAt(position)
                allAlarms.postValue(alarms!!)
            }

        }
    }

    fun setAlarmData(_hour : Int, _min : Int, _ampm : Boolean, _active : Boolean, _timeZone : Int, _days : String) {
        hour = _hour
        min = _min
        ampm = _ampm
        active = _active
        timeZone = _timeZone
        days = _days

        mScope.launch(Dispatchers.IO) {
            data.postValue(
                AlarmInfo(hour, min, ampm, active, timeZone, days)
            )

            insert()
        }
    }

    fun setAlarmData() {
        mScope.launch(Dispatchers.IO) {
            data.postValue(
                AlarmInfo(hour, min, ampm, active, timeZone, days)
            )

            insert()
        }
    }

    fun getTempAlarmData() : AlarmInfo {
        return AlarmInfo(hour, min, ampm, active, timeZone, days)
    }

    fun setTempAlarmData(_hour : Int, _min : Int, _ampm : Boolean, _active : Boolean, _timeZone : Int, _days : String) {
        hour = _hour
        min = _min
        ampm = _ampm
        active = _active
        timeZone = _timeZone
        days = _days
    }

    fun deleteAll() {
        mScope.launch(Dispatchers.IO) {
            mAlarmDao.deleteAll()
            allAlarms.postValue(arrayListOf())
        }
    }

    fun getAlarm(alarmID: Int) {
        mScope.launch(Dispatchers.IO) {
            val alarmTable = mAlarmDao.getAlarm(alarmID)
            if (alarmTable != null) {
                hour = alarmTable.hour
                min = alarmTable.min
                ampm = alarmTable.ampm
                active = alarmTable.active
                timeZone = alarmTable.timeZone
                days = alarmTable.days
                id = alarmTable.alarmID
            }

            data.postValue(
                AlarmInfo(hour, min, ampm, active, timeZone, days)
            )
        }
    }

    fun getAllAlarm() {

        mScope.launch(Dispatchers.IO) {
            val alarms : ArrayList<AlarmInfo> = arrayListOf()
            val alarmTable = mAlarmDao.getAllAlarm()
            for (table in alarmTable) {
                val alarmInfo = AlarmInfo(table.hour, table.min, table.ampm, table.active, table.timeZone, table.days)
                alarmInfo.alarmID = table.alarmID
                alarms.add(alarmInfo)
            }

            allAlarms.postValue(alarms)
        }
    }

    fun updateAlarm(alarmID: Int, _hour : Int, _min : Int, _ampm : Boolean, _active : Boolean, _timeZone : Int, _days : String) {
        hour = _hour
        min = _min
        ampm = _ampm
        active = _active
        timeZone = _timeZone
        days = _days

        mScope.launch(Dispatchers.IO) {
            data.postValue(
                AlarmInfo(hour,min, ampm, active, timeZone, days)
            )

            mAlarmDao.updateAlarm(alarmID, hour, min, ampm, active, timeZone, days)
        }
    }

    @WorkerThread
    suspend fun insert() {
        mAlarmDao.insert(
            AlarmTable(
                0,
                hour,
                min,
                ampm,
                active,
                timeZone,
                days
            )
        )
    }

    companion object {
        private var mInstance : AlarmRepo? = null
        private lateinit var mScope: CoroutineScope
        @Synchronized
        fun getInstance(alarmDao: AlarmDao,
                        scope: CoroutineScope) : AlarmRepo {
            mScope = scope
            return mInstance?: synchronized(this) {
                val instance = AlarmRepo(alarmDao)
                mInstance = instance
                instance
            }
        }
    }
}
package com.example.alarmapp

import androidx.lifecycle.*
import android.content.Context
import java.lang.IllegalArgumentException

class AlarmViewModel(repo : AlarmRepo) : ViewModel(){

    private val mAlarmData : MutableLiveData<AlarmInfo> = repo.data
    private var mAlarmListData : ArrayList<AlarmInfo>? = null
    private val mAlarmRepo : AlarmRepo = repo

    fun clearData() {
        mAlarmRepo.clearData()
    }

    fun deleteAlarmData(alarmID : Int, position: Int) {
        if (mAlarmListData != null) {
            mAlarmListData!!.removeAt(position)
        }
        mAlarmRepo.deleteAlarm(alarmID)
    }

    fun setAlarmData(hour : Int, min: Int, ampm : Boolean, active : Boolean, timeZone : Int, days : String) {
        mAlarmRepo.setAlarmData(hour, min, ampm, active, timeZone, days)
    }

    fun setAlarmData(alarm : AlarmInfo) {
        mAlarmRepo.setAlarmData(alarm.Hour, alarm.Min, alarm.AMPM, alarm.Activated, alarm.TimeZone, alarm.Days)
    }

    fun updateAlarm(alarmID : Int, hour : Int, min: Int, ampm : Boolean, active : Boolean, timeZone : Int, days : String) {
        mAlarmRepo.updateAlarm(alarmID, hour, min, ampm, active, timeZone, days)
    }

    fun getAlarm(alarmID: Int) {
        mAlarmRepo.getAlarm(alarmID)
    }

    fun getAllAlarm() : ArrayList<AlarmInfo> {
        if (mAlarmListData == null)
            mAlarmListData = mAlarmRepo.getAllAlarm()
        return mAlarmListData!!
    }

    fun getTempAlarmData() : AlarmInfo{
        return mAlarmRepo.getTempAlarmData()
    }

    fun setTempAlarmData(hour : Int, min: Int, ampm : Boolean, active : Boolean, timeZone : Int, days : String) {
        mAlarmRepo.setTempAlarmData(hour, min, ampm, active, timeZone, days)
    }

    fun deleteAlarmTable() {
        mAlarmRepo.deleteAll()
    }

    val data : LiveData<AlarmInfo> get() = mAlarmData
}

class AlarmViewModelFactory (private val repository : AlarmRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create (modelClass : Class<T>):T {
        if (modelClass.isAssignableFrom(AlarmViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlarmViewModel(repository) as T
        }
        throw IllegalArgumentException("UNKNOWN VIEWMODEL CLASS")
    }
}
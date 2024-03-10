package com.example.alarmapp

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class AlarmDataMonitor : Application(){
    val applicationScope  = CoroutineScope(SupervisorJob())

    val database by lazy {AlarmDatabase.getDatabase(this, applicationScope)}
    val repository by lazy {AlarmRepo.getInstance(database.alarmDao(), applicationScope)}
}
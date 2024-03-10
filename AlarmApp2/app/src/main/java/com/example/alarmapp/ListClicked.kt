package com.example.alarmapp

interface ListClicked {
    fun onItemClick(position : Int, alarmInfo: AlarmInfo)

    fun onItemLongClick(alarmID : Int, position : Int)
}
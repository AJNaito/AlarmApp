package com.example.alarmapp

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class AlarmInfo (var Hour : Int, var Min : Int, var AMPM : Boolean, var Activated : Boolean, var TimeZone : Int, var Days : String, var alarmID :Int = 0)
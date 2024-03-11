package com.example.alarmapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.VIBRATOR_MANAGER_SERVICE
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.Vibrator
import android.net.Uri
import android.os.VibratorManager
import androidx.annotation.RequiresApi


class AlarmReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context, intent: Intent?) {
//        val vibManage = context!!.getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
//        val vibrator = vibManage.defaultVibrator
//
//        vibrator.vibrate()
        var alarmURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        if (alarmURI == null)
            alarmURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val ringtone = RingtoneManager.getRingtone(context, alarmURI)
        ringtone.play()
    }
}
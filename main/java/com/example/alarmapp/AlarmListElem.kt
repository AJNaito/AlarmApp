package com.example.alarmapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import java.io.Serializable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class AlarmListElem (private val alarmData : ArrayList<AlarmInfo>, val listener: ListClicked) : RecyclerView.Adapter<AlarmListElem.ListViewHolder>() {

    //val alarmData : ArrayList<AlarmInfo>? = null
    var context : Context? = null

    override fun getItemCount(): Int {
        return alarmData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.alarm_list_elem, parent, false)
        context = parent.context
        return ListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentAlarm = alarmData[position]
        val time = currentAlarm.Hour.toString() + ":" + currentAlarm.Min.toString()

        holder.alarmText.text = time
        holder.active.isActivated = currentAlarm.Activated
        holder.timeZone.text = currentAlarm.TimeZone.toString()
        holder.days.text = currentAlarm.Days
        holder.AMPM.text = if (currentAlarm.AMPM) "AM" else "PM"
    }

    inner class ListViewHolder (view : View) : ViewHolder(view), View.OnClickListener, View.OnLongClickListener {
        val alarmText: TextView = view.findViewById(R.id.AlarmText)
        val AMPM: TextView = view.findViewById(R.id.AmPm)
        val timeZone: TextView = view.findViewById(R.id.TimeZone)
        val active = view.findViewById<Switch>(R.id.ToggleAlarm)
        val days: TextView = view.findViewById(R.id.DaysAlarm)

        init {
            view.setOnClickListener(this)
            view.setOnLongClickListener(this)
        }



        override fun onClick(view: View?) {
            // inform activity to go to add screen to modify
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.onItemClick(adapterPosition, alarmData[adapterPosition])
            }
        }

        override fun onLongClick(view: View?): Boolean {
            if (adapterPosition != RecyclerView.NO_POSITION && context != null) {
                // delete current
                val alarmDeleted = alarmData.removeAt(adapterPosition)

                ScheduleAlarm.removeAlarm(context!!, alarmDeleted.alarmID)
                listener.onItemLongClick(alarmDeleted.alarmID, adapterPosition)
                notifyItemRemoved(adapterPosition)
                return true
            }

            return false
        }
    }
}
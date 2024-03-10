package com.example.alarmapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import com.example.alarmapp.databinding.FragmentAlarmScreenBinding

/**
 * A simple [Fragment] subclass.
 * Use the [AlarmScreen.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlarmScreen : Fragment(), ListClicked, View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var _binding : FragmentAlarmScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var alarmViewModel : AlarmViewModel

    private var alarmList : ArrayList<AlarmInfo>?  = null

    private var timeDisplay : TextView? = null
    private var recyclerView : RecyclerView? = null
    private var addButton : ImageButton? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlarmScreenBinding.inflate(inflater, container, false)
        val view = binding.root

        alarmViewModel = (activity as MainActivity).mAlarmViewModel

        timeDisplay = view.findViewById(R.id.CurrentTimeZone) as TextView
        recyclerView = view.findViewById(R.id.AlarmList) as RecyclerView
        addButton = view.findViewById(R.id.AddAlarm) as ImageButton

        addButton!!.setOnClickListener(this)
        alarmList = alarmViewModel.getAllAlarm()
        val adapter = AlarmListElem(alarmList!!, this)

        recyclerView!!.adapter = adapter

        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if (timeDisplay != null) {
            timeDisplay!!.text = TimeZone.getDefault().toString()
        }

        if (recyclerView != null) {
            recyclerView!!.adapter = AlarmListElem(alarmViewModel.getAllAlarm(), this)
        }
    }


    override fun onItemClick(position: Int, alarmInfo: AlarmInfo) {
        // navigate to add alarm with alarm info
        alarmViewModel.setTempAlarmData(alarmInfo.Hour, alarmInfo.Min, alarmInfo.AMPM, alarmInfo.Activated, alarmInfo.TimeZone, alarmInfo.Days)

        findNavController().navigate(R.id.AddScreen)
    }

    override fun onItemLongClick(alarmID : Int, position: Int) {
        // delete alarm
        alarmViewModel.deleteAlarmData(alarmID, position)
    }

    override fun onClick(p0: View?) {
        // navigate to add alarm
        alarmViewModel.clearData()

        findNavController().navigate(R.id.AddScreen) // go to add screen no arguments
    }


}

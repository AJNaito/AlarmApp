package com.example.alarmapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.Observer
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

    private var alarmList : ArrayList<AlarmInfo> = arrayListOf()

    private var timeDisplay : TextView? = null
    private var recyclerView : RecyclerView? = null
    private var addButton : ImageButton? = null

    private var adapter : AlarmListElem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_alarm_screen, container, false)

        alarmViewModel = (activity as MainActivity).mAlarmViewModel
        alarmViewModel.allData.observe(viewLifecycleOwner, alarmListDataObserver)

        timeDisplay = view.findViewById(R.id.CurrentTimeZone) as TextView
        recyclerView = view.findViewById(R.id.AlarmList) as RecyclerView
        addButton = view.findViewById(R.id.AddAlarm) as ImageButton

        addButton!!.setOnClickListener(this)

        adapter = AlarmListElem(alarmList, this)
        alarmViewModel.getAllAlarm()

        recyclerView!!.adapter = adapter

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private val alarmListDataObserver : Observer<ArrayList<AlarmInfo>> =
        Observer {
            mAlarmData ->
            if (adapter != null) {
                alarmList = mAlarmData
                adapter!!.notifyDataSetChanged()
            }
        }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if (timeDisplay != null) {
            timeDisplay!!.text = TimeZone.getDefault().toString()
        }

        if (recyclerView != null) {
            recyclerView!!.adapter = AlarmListElem(arrayListOf(), this)
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

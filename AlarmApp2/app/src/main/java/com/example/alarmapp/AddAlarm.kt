package com.example.alarmapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.Environment
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import java.util.*
import com.example.alarmapp.databinding.FragmentAddAlarmBinding

/**
 * A simple [Fragment] subclass.
 * Use the [AddAlarm.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddAlarm : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentAddAlarmBinding? = null
    private val binding get() = _binding!!

    private lateinit var alarmViewModel : AlarmViewModel

    // Buttons
    private var saveButton : Button? = null
    private var cancelButton : Button? = null

    // Number Pickers
    private var hourPick : NumberPicker? = null
    private var minPick : NumberPicker? = null
    private var ampmPick : NumberPicker? = null

    // Spinners
    private var timeZonePick : Spinner? = null

    // Checkboxes
    private var mon : CheckBox? = null
    private var tue : CheckBox? = null
    private var wed : CheckBox? = null
    private var thur : CheckBox? = null
    private var fri : CheckBox? = null
    private var sat : CheckBox? = null
    private var sun : CheckBox? = null

    private var index : Int? = null
    private var daysChecked : Array<Boolean> = arrayOf(false, false, false, false, false, false, false)
    private val days : Array<String> = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        alarmViewModel = (requireActivity() as MainActivity).mAlarmViewModel

        _binding = FragmentAddAlarmBinding.inflate(inflater, container, false)
        val view = binding.root
        saveButton = view.findViewById(R.id.SaveButton) as Button
        cancelButton = view.findViewById(R.id.CancelButton) as Button

        hourPick = view.findViewById(R.id.HourSelect) as NumberPicker
        minPick = view.findViewById(R.id.Minute) as NumberPicker
        ampmPick = view.findViewById(R.id.AmPm) as NumberPicker

        timeZonePick = view.findViewById(R.id.SelectTimeZone) as Spinner

        mon = view.findViewById(R.id.Mon) as CheckBox
        tue = view.findViewById(R.id.Tues) as CheckBox
        wed = view.findViewById(R.id.Wed) as CheckBox
        thur = view.findViewById(R.id.Thurs) as CheckBox
        fri = view.findViewById(R.id.Fri) as CheckBox
        sat = view.findViewById(R.id.Sat) as CheckBox
        sun = view.findViewById(R.id.Sun) as CheckBox

        mon!!.setOnClickListener(this)
        tue!!.setOnClickListener(this)
        wed!!.setOnClickListener(this)
        thur!!.setOnClickListener(this)
        fri!!.setOnClickListener(this)
        sat!!.setOnClickListener(this)
        sun!!.setOnClickListener(this)

        hourPick!!.minValue = 1
        hourPick!!.maxValue = 12
        hourPick!!.wrapSelectorWheel = true
        hourPick!!.value = 12

        minPick!!.minValue = 0
        minPick!!.maxValue = 59
        minPick!!.wrapSelectorWheel = true

        ampmPick!!.minValue = 0
        ampmPick!!.maxValue = 1
        ampmPick!!.displayedValues = resources.getStringArray(R.array.AMPM)

        ArrayAdapter.createFromResource(
            requireActivity(), R.array.TimeZones, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            timeZonePick!!.adapter = adapter
        }

        saveButton!!.setOnClickListener(this)
        cancelButton!!.setOnClickListener(this)

        SetValues()

        return view
    }

    private fun SetValues() {
            val alarmInfo = alarmViewModel.getTempAlarmData()

            //Set Default Values
            hourPick!!.value = alarmInfo.Hour
            minPick!!.value = alarmInfo.Min
            if (!alarmInfo.AMPM) ampmPick!!.value = 1

            timeZonePick!!.setSelection(alarmInfo.TimeZone)

            val days = alarmInfo.Days.split(",")
            for (day in days) {
                if (day == "Mon") {
                    daysChecked[0] = true
                    mon!!.isChecked = true
                }
                if (day == "Tue") {
                    daysChecked[1] = true
                    tue!!.isChecked = true
                }
                if (day == "Wed") {
                    daysChecked[2] = true
                    wed!!.isChecked = true
                }
                if (day == "Thu") {
                    daysChecked[3] = true
                    thur!!.isChecked = true
                }
                if (day == "Fri") {
                    daysChecked[4] = true
                    fri!!.isChecked = true
                }
                if (day == "Sat") {
                    daysChecked[5] = true
                    sat!!.isChecked = true
                }
                if (day == "Sun") {
                    daysChecked[6] = true
                    sun!!.isChecked = true
                }

            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        alarmViewModel.setTempAlarmData(hourPick!!.value, minPick!!.value, ampmPick!!.value == 0, true, timeZonePick!!.selectedItemPosition, stringDays())
        SetValues()
    }

    private fun stringDays() : String {
        var daysSel = ""
        for (i in daysChecked.indices) {
            if (daysChecked[i]) {
                daysSel += days[i] + ","
            }
        }

        daysSel = daysSel.substring(0, daysSel.length - 1)

        return daysSel
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.Mon -> {
                daysChecked[0] = mon!!.isChecked
            }

            R.id.Tues -> {
                daysChecked[1] = tue!!.isChecked
            }

            R.id.Wed -> {
                daysChecked[2] = wed!!.isChecked
            }

            R.id.Thurs -> {
                daysChecked[3] = thur!!.isChecked
            }

            R.id.Fri -> {
                daysChecked[4] = fri!!.isChecked
            }

            R.id.Sat -> {
                daysChecked[5] = sat!!.isChecked
            }

            R.id.Sun -> {
                daysChecked[6] = sun!!.isChecked
            }

            R.id.SaveButton -> {
                // Check if days are selected
                val daysSel = stringDays()

                if (daysSel.isNotBlank()) {
                    val alarm = AlarmInfo(hourPick!!.value, minPick!!.value, ampmPick!!.value == 0, true, timeZonePick!!.selectedItemPosition, daysSel)
                    alarmViewModel.setAlarmData(alarm)
                    ScheduleAlarm.scheduleAlarm(requireContext(), alarm)

                    findNavController().navigate(R.id.MainScreen)
                } else {
                    Toast.makeText(requireActivity(), "Must have at least one day selected", Toast.LENGTH_SHORT).show()
                }
            }

            R.id.CancelButton -> {
                // navigate back to main
                // change after
                findNavController().navigate((R.id.MainScreen))
            }
        }
    }
}
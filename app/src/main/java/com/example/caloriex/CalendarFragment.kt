package com.example.caloriex

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class CalendarFragment : Fragment() {

    private var selectedDate: String = getCurrentDayString()
    private lateinit var calendarView: CalendarView
    private lateinit var okTextView: TextView
    private lateinit var cancelTextView: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        calendarView = view.findViewById(R.id.calendar_view)
        okTextView = view.findViewById(R.id.ok_textview)
        cancelTextView = view.findViewById(R.id.cancel_textview)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = constructDate(year, month + 1, dayOfMonth)
        }

        // No new date just go back to dashboard
        cancelTextView.setOnClickListener {
            findNavController().popBackStack()
        }

        // Sends the new date
        okTextView.setOnClickListener {
            intentToDashboard(selectedDate)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }
    }

    private fun intentToDashboard(newDate: String) {
        val bundle = Bundle()
        bundle.putString("date", newDate)

       // val destinationFragment = DashboardFragment()
       // destinationFragment.arguments = bundle

      //  findNavController().navigate(R.id.dashboard)

    }
}

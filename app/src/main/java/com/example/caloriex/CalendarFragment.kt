package com.example.caloriex

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CalendarFragment : Fragment() {

    private var selectedDate: String = getCurrentDayString()
    private lateinit var calendarView: CalendarView
    private lateinit var okTextView: TextView
    private lateinit var cancelTextView: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        calendarView = view.findViewById(R.id.calendar_view)
        okTextView = view.findViewById(R.id.ok_textview)
        cancelTextView = view.findViewById(R.id.cancel_textview)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    selectedDate = constructDate(year, month, dayOfMonth)

                    Log.d("selectedDate", "$selectedDate")

                    if (userEmail != null) {
                        val date = CalendarDate(date = selectedDate)
                        Firebase.database.getReference("/${encodeEmail(userEmail)}/calendarDate").setValue(date)
                    }
                }
            }
        }

        // No new date just go back to dashboard
        cancelTextView.setOnClickListener {
            findNavController().popBackStack()
        }

        // Sends the new date
        okTextView.setOnClickListener {
            findNavController().navigate(R.id.dashboardFragment)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Do nothing
        }
    }
}

package com.example.caloriex

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartTrackingFragment : Fragment() {

    private lateinit var startTrackingBtn: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_start_tracking, container, false)
        startTrackingBtn = view.findViewById(R.id.start_tracking_button)

        // Set click listener on the button to launch the dashboard:
        startTrackingBtn.setOnClickListener {
            lifecycleScope.launch {
                delay(1500) // Replace Handler with delay coroutine
                withContext(Dispatchers.Main) {
                    activity?.runOnUiThread {// Navigate on the main thread
                        findNavController().navigate(R.id.action_startTrackingFragment_to_dashboardFragment)
                    }
                }
            }
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

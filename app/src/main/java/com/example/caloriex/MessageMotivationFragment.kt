package com.example.caloriex

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class MessageMotivationFragment : Fragment() {

    private lateinit var definitelyBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_message_motivation, container, false)
        changeActivity(view)
        return view
    }

    private fun changeActivity(view: View) {
        definitelyBtn = view.findViewById(R.id.definitely_button)

        // Waiting for the click event from user. Once done so, this will prompt DashboardActivity
        definitelyBtn.setOnClickListener {
            findNavController().navigate(R.id.action_messageMotivationFragment_to_dashboardFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Do nothing
        }
    }
}

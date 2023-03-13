package com.example.caloriex

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class GetStartedFragment : Fragment() {

    private lateinit var getStartedBtn: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_get_started, container, false)

        getStartedBtn = view.findViewById(R.id.get_started_button)

        // Waiting for the click event from user. Once done so, this will prompt AccountSignInActivity
        getStartedBtn.setOnClickListener {
            findNavController().navigate(R.id.action_getStartedFragment_to_accountSignInFragment)
        }

        return view
    }
}

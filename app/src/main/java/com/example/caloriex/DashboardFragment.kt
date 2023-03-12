package com.example.caloriex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class DashboardFragment : Fragment() {

    private lateinit var navigationView: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        navigationView = view.findViewById(R.id.bottom_navigation)
        navController = findNavController()
        navigationView.setupWithNavController(navController)

        // Customize the label visibility mode
        navigationView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_SELECTED

        // Customize the item selection behavior
        navigationView.setOnItemSelectedListener { menuItem ->
            NavigationUI.onNavDestinationSelected(menuItem, navController)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Do nothing
        }
    }

    override fun onResume() {
        super.onResume()
        // Set the selected item of bottom navigation view when the fragment is resumed
        navigationView.menu.findItem(navController.currentDestination!!.id).isChecked = true
    }
}

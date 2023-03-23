package com.example.caloriex

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class SettingsFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var navigationView: BottomNavigationView

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        navigationView = view.findViewById(R.id.bottom_navigation)

        navController = findNavController()
        navigationView.setupWithNavController(navController)

        val recyclerView = view.findViewById<RecyclerView>(R.id.settings_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ArrayList of class ItemsViewModel
        val data = ArrayList<ItemsViewModel>()

        data.add(ItemsViewModel(R.drawable.ic_profile_details_foreground, "Profile Details", R.drawable.ic_right_arrow_foreground))
        data.add(ItemsViewModel(R.drawable.ic_energy_settings_foreground, "Energy Settings", R.drawable.ic_right_arrow_foreground))
        data.add(ItemsViewModel(R.drawable.ic_macronutrient_ratios_foreground, "Macronutrient Ratios", R.drawable.ic_right_arrow_foreground))
        data.add(ItemsViewModel(R.drawable.ic_logout_foreground, "Logout", R.drawable.ic_right_arrow_foreground))

        val adapter = RecyclerViewAdapter(data,navController)
        recyclerView.adapter = adapter

        // Customize the label visibility mode
        navigationView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        navigationView.menu.findItem(R.id.menu_settings).isChecked = true

        val iconColor = resources.getColorStateList(R.color.menu_selector_icon, null)
        navigationView.itemIconTintList = iconColor

        // Customize the item selection behavior
        navigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_diary -> {
                    navigationView.menu.findItem(R.id.menu_diary).setIcon(R.drawable.ic_diary_foreground)

                    // Navigate to destination1
                    navController.navigate(R.id.action_settingsFragment_to_dashboardFragment)
                    true
                }

                // Need one for menu_plus

                R.id.menu_charts -> {
                    navigationView.menu.findItem(R.id.menu_charts).setIcon(R.drawable.ic_charts_foreground)

                    // Navigate to destination2
                    navController.navigate(R.id.action_settingsFragment_to_chartsFragment)
                    true
                }
                // Add more destinations here...
                else -> false
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


    override fun onResume() {
        super.onResume()
    }
}
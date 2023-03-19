package com.example.caloriex

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class ChartsFragment : Fragment() {

    private lateinit var navigationView: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var historyBarChart: BarChart

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_charts, container, false)

        navigationView = view.findViewById(R.id.bottom_navigation)
        historyBarChart = view.findViewById(R.id.barchart)
        navController = findNavController()
        navigationView.setupWithNavController(navController)
        prefilledTextField(view)

        // Customize the label visibility mode
        navigationView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        navigationView.menu.findItem(R.id.menu_charts).isChecked = true

        val iconColor = resources.getColorStateList(R.color.menu_selector_icon, null)
        navigationView.itemIconTintList = iconColor

        // Customize the item selection behavior
        navigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_diary -> {
                    navigationView.menu.findItem(R.id.menu_diary).setIcon(R.drawable.ic_diary_foreground)

                    // Navigate to destination1
                    navController.navigate(R.id.action_chartsFragment_to_dashboardFragment)
                    true
                }

                // Need one for menu_plus

                R.id.menu_charts -> {
                    navigationView.menu.findItem(R.id.menu_charts).setIcon(R.drawable.ic_charts_foreground)

                    // Navigate to destination2
                    navController.navigate(R.id.chartsFragment)
                    true
                }

                R.id.menu_settings -> {
                    navigationView.menu.findItem(R.id.menu_settings).setIcon(R.drawable.ic_settings_foreground)

                    // Navigate to destination2
                    navController.navigate(R.id.action_chartsFragment_to_settingsFragment)
                    true
                }
                // Add more destinations here...
                else -> false
            }
        }

        val entries: ArrayList<BarEntry> = ArrayList()
        entries.add(BarEntry(70f, 70f))

        val dataSet = BarDataSet(entries, "Mobile OS")
        dataSet.setDrawIcons(false)
        dataSet.color = Color.rgb(135,182,120)

        val data = BarData(dataSet)
        data.setValueTextColor(Color.WHITE)

        setupBarChart(historyBarChart, data)

        return view
    }

    private fun setupBarChart(barChart: BarChart, data: BarData) {
        barChart.description.isEnabled = false
        barChart.dragDecelerationFrictionCoef = 0.95f
        barChart.isHighlightPerTapEnabled = true
        barChart.animateY(1400, Easing.EaseInOutQuad)
        barChart.legend.isEnabled = false
        barChart.data = data
        barChart.xAxis.textColor = Color.WHITE
        barChart.axisLeft.textColor = Color.WHITE
        barChart.axisRight.textColor = Color.WHITE
        barChart.highlightValues(null)
        barChart.isHighlightPerTapEnabled = false
        barChart.invalidate()
    }

    private fun prefilledTextField(view: View) {
        val monthAutocompleteTextView =
            view.findViewById<AutoCompleteTextView>(R.id.month_dropdown)
        val monthOptions = resources.getStringArray(R.array.month)

        var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, monthOptions)
        monthAutocompleteTextView.setAdapter(adapter)

        monthAutocompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedMonth = parent.getItemAtPosition(position) as String
            val startIndex = monthAutocompleteTextView.text.indexOf(selectedMonth)
            val endIndex = startIndex + selectedMonth.length
            monthAutocompleteTextView.setSelection(startIndex.coerceAtMost(endIndex))
        }

        val chartOfAutocompleteTextView =
            view.findViewById<AutoCompleteTextView>(R.id.chart_of_dropdown)
        val chartOfOptions = resources.getStringArray(R.array.chart_of)

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, chartOfOptions)
        chartOfAutocompleteTextView.setAdapter(adapter)

        chartOfAutocompleteTextView.setOnItemClickListener { parent, view, position, id ->
            chartOfAutocompleteTextView.setSelection(position)
        }
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
       // navigationView.menu.findItem(navController.currentDestination!!.id).isChecked = true
    }
}

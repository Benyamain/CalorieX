package com.example.caloriex

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView

private const val TAG = "ANNOYING"

class DashboardFragment : Fragment() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var caloriePieChart: PieChart
    private lateinit var proteinPieChart: PieChart
    private lateinit var carbsPieChart: PieChart
    private lateinit var fatPieChart: PieChart
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var selectedDate: String
    private lateinit var calendarTv: TextView
    private lateinit var imageIv: ImageView


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        bottomNavigationView = view.findViewById(R.id.bottom_navigation)
        caloriePieChart = view.findViewById(R.id.pieChartCaloricBudget)
        proteinPieChart = view.findViewById(R.id.pieChartProtein)
        carbsPieChart = view.findViewById(R.id.pieChartCarbs)
        fatPieChart = view.findViewById(R.id.pieChartFat)
        toolbar = view.findViewById(R.id.toolbar)
        calendarTv = view.findViewById(R.id.calendar_text_view)
        imageIv = view.findViewById(R.id.calendar_image_view)

        navController = findNavController()
        bottomNavigationView.setupWithNavController(navController)

        imageIv.setOnClickListener {
            navController.navigate(R.id.action_dashboardFragment_to_calendarFragment)
        }

        // Customize the label visibility mode
        bottomNavigationView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_SELECTED

        // Customize the item selection behavior
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_diary -> {
                    // Navigate to destination1
                    navController.navigate(R.id.dashboardFragment)
                    true
                }

                // Need one for menu_plus

                R.id.menu_charts -> {
                    // Navigate to destination2
                    navController.navigate(R.id.action_dashboardFragment_to_chartsFragment)
                    true
                }

                R.id.menu_settings -> {
                    // Navigate to destination2
                    navController.navigate(R.id.action_dashboardFragment_to_logoutFragment)
                    true
                }
                // Add more destinations here...
                else -> false
            }
        }

        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(70f))

        val dataSet = PieDataSet(entries, "Mobile OS")
        dataSet.setDrawIcons(false)
        dataSet.color = Color.rgb(135,182,120)

        val data = PieData(dataSet)

        setupPieChart(caloriePieChart, data)
        setupPieChart(proteinPieChart, data)
        setupPieChart(carbsPieChart, data)
        setupPieChart(fatPieChart, data)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Do nothing
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()

        // Read the date when the user navigates back to the DashboardFragment from the CalendarFragment
        val bundle = arguments
        if (bundle != null && bundle.containsKey("date")) {
            readDate()
        }
    }

    private fun setupPieChart(pieChart: PieChart, data: PieData) {
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.dragDecelerationFrictionCoef = 0.95f
        pieChart.setDrawCenterText(true)
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.rgb(45,42,62))
        pieChart.rotationAngle = 0f
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true
        pieChart.animateY(1400, Easing.EaseInOutQuad)
        pieChart.legend.isEnabled = false
        pieChart.data = data
        pieChart.highlightValues(null)
        pieChart.invalidate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun readDate() {
        selectedDate = arguments?.getString("date").toString()

        Log.d(TAG, "readDate: $selectedDate")

        val correctDateFormat = selectedDate
        calendarTv.text =  correctDateFormat.makeDateReadable()
    }
}

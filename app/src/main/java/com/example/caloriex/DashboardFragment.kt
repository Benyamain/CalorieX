package com.example.caloriex

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "ANNOYING"
private val PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)

class DashboardFragment : Fragment() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var appearBottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var caloriePieChart: PieChart
    private lateinit var proteinPieChart: PieChart
    private lateinit var carbsPieChart: PieChart
    private lateinit var fatPieChart: PieChart
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var selectedDate: String
    private lateinit var calendarTv: TextView
    private lateinit var imageIv: ImageView
    private lateinit var dashboardRecyclerView: RecyclerView
    private lateinit var dashboardItemsAdapter: DashboardItemsAdapter
    private var appearBottomCounter = 0

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission request granted", Toast.LENGTH_LONG).show()
                navigateToCamera()
            } else {
                Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        bottomNavigationView = view.findViewById(R.id.bottom_navigation)
        appearBottomNavigationView = view.findViewById(R.id.appear_bottom_navigation)
        caloriePieChart = view.findViewById(R.id.pieChartCaloricBudget)
        proteinPieChart = view.findViewById(R.id.pieChartProtein)
        carbsPieChart = view.findViewById(R.id.pieChartCarbs)
        fatPieChart = view.findViewById(R.id.pieChartFat)
        toolbar = view.findViewById(R.id.toolbar)
        calendarTv = view.findViewById(R.id.calendar_text_view)
        imageIv = view.findViewById(R.id.calendar_image_view)

        navController = findNavController()
        bottomNavigationView.setupWithNavController(navController)
        appearBottomNavigationView.setupWithNavController(navController)

        dashboardRecyclerView = view.findViewById(R.id.dashboard_recycler_view)
        dashboardRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ArrayList of class DashboardItems
        val dashboardData = ArrayList<DashboardItems>()

        // Testing purposes this is not actual way it will be done in the final version of the app
        for (i in 1..20) {
            dashboardData.add(DashboardItems(R.drawable.gray_logo, "Food Name $i", "999 g", "1450.2", "kcal"))
        }

        dashboardItemsAdapter = DashboardItemsAdapter(dashboardData,navController,appearBottomNavigationView)
        dashboardRecyclerView.adapter = dashboardItemsAdapter

        imageIv.setOnClickListener {
            navController.navigate(R.id.action_dashboardFragment_to_calendarFragment)
            appearBottomCounter = 0
        }

        // Customize the label visibility mode
        bottomNavigationView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        appearBottomNavigationView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        bottomNavigationView.menu.findItem(R.id.menu_diary).isChecked = true

        // Customize the item selection behavior
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_diary -> {
                    bottomNavigationView.menu.findItem(R.id.menu_diary)
                        .setIcon(R.drawable.ic_diary_foreground)

                    if (appearBottomNavigationView.visibility == View.VISIBLE) {
                        appearBottomNavigationView.startAnimation(
                            AnimationUtils.loadAnimation(
                                requireContext(),
                                R.anim.slide_up_fast
                            )
                        )
                        appearBottomNavigationView.visibility = View.INVISIBLE
                        bottomNavigationView.itemIconTintList = AppCompatResources.getColorStateList(requireContext(), R.color.menu_selector_icon)
                        appearBottomNavigationView.menu.findItem(R.id.menu_add_weight).isChecked = false
                        appearBottomNavigationView.menu.findItem(R.id.menu_snap_food).isChecked = false
                        appearBottomNavigationView.menu.findItem(R.id.menu_add_food).isChecked = false
                        appearBottomNavigationView.menu.findItem(R.id.menu_not_visible).isChecked = false
                        appearBottomCounter = 0
                    }
                    true
                }

                R.id.menu_plus -> {
                    bottomNavigationView.menu.findItem(R.id.menu_plus)
                        .setIcon(R.drawable.ic_plus_foreground)

                    appearBottomCounter++
                    if (appearBottomCounter == 1) {
                        appearBottom()
                    }

                    true
                }

                R.id.menu_charts -> {
                    bottomNavigationView.menu.findItem(R.id.menu_charts)
                        .setIcon(R.drawable.ic_charts_foreground)

                    // Navigate to destination2
                    navController.navigate(R.id.action_dashboardFragment_to_chartsFragment)
                    true
                }

                R.id.menu_settings -> {
                    bottomNavigationView.menu.findItem(R.id.menu_settings)
                        .setIcon(R.drawable.ic_settings_foreground)

                    // Navigate to destination2
                    navController.navigate(R.id.action_dashboardFragment_to_settingsFragment)
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
        dataSet.color = Color.rgb(135, 182, 120)

        val data = PieData(dataSet)
        data.setValueTextColor(Color.WHITE)

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
        if ((bundle != null) && bundle.containsKey("date")) {
            readDate()
        }
    }

    private fun appearBottom() {
        appearBottomNavigationView.visibility = View.VISIBLE
        appearBottomNavigationView.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.slide_down_slow
            )
        )
        appearBottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_snap_food -> {
                    appearBottomNavigationView.menu.findItem(R.id.menu_snap_food)
                        .setIcon(R.drawable.ic_snap_food_foreground)

                    requestCameraPermission()

                    appearBottomCounter = 0
                    appearBottomNavigationView.menu.findItem(R.id.menu_snap_food).isChecked = true
                    appearBottomNavigationView.menu.findItem(R.id.menu_add_food).isChecked = false
                    appearBottomNavigationView.menu.findItem(R.id.menu_add_weight).isChecked = false
                    appearBottomNavigationView.menu.findItem(R.id.menu_not_visible).isChecked = false
                    bottomNavigationView.itemIconTintList = AppCompatResources.getColorStateList(requireContext(), R.color.gray)
                    true
                }

                R.id.menu_add_food -> {
                    appearBottomNavigationView.menu.findItem(R.id.menu_add_food)
                        .setIcon(R.drawable.ic_add_food_foreground)

                    navController.navigate(R.id.action_dashboardFragment_to_searchFoodFragment)
                    appearBottomCounter = 0
                    appearBottomNavigationView.menu.findItem(R.id.menu_add_food).isChecked = true
                    appearBottomNavigationView.menu.findItem(R.id.menu_snap_food).isChecked = false
                    appearBottomNavigationView.menu.findItem(R.id.menu_add_weight).isChecked = false
                    appearBottomNavigationView.menu.findItem(R.id.menu_not_visible).isChecked = false
                    // Add the logic for NutritionInfoFragment once user clicks some food and wants the nutrition data for it
                    true
                }

                R.id.menu_add_weight -> {
                    appearBottomNavigationView.menu.findItem(R.id.menu_add_weight)
                        .setIcon(R.drawable.ic_add_weight_foreground)

                    navController.navigate(R.id.action_dashboardFragment_to_addWeightFragment)
                    appearBottomCounter = 0
                    appearBottomNavigationView.menu.findItem(R.id.menu_add_weight).isChecked = true
                    appearBottomNavigationView.menu.findItem(R.id.menu_snap_food).isChecked = false
                    appearBottomNavigationView.menu.findItem(R.id.menu_add_food).isChecked = false
                    appearBottomNavigationView.menu.findItem(R.id.menu_not_visible).isChecked = false
                    true
                }
                // Add more destinations here...
                else -> false
            }
        }
    }

    private fun setupPieChart(pieChart: PieChart, data: PieData) {
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.dragDecelerationFrictionCoef = 0.95f
        pieChart.setDrawCenterText(true)
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.rgb(45, 42, 62))
        pieChart.rotationAngle = 0f
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true
        pieChart.animateY(1400, Easing.EaseInOutQuad)
        pieChart.legend.isEnabled = false
        pieChart.data = data
        pieChart.highlightValues(null)
        pieChart.invalidate()
    }

    private fun navigateToCamera() {
        lifecycleScope.launchWhenStarted {
           navController.navigate(R.id.action_permissionsFragment_to_cameraFragment)
        }
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun readDate() {
        selectedDate = arguments?.getString("date").toString()

        Log.d(TAG, "readDate: $selectedDate")

        val today = getCurrentDayString()

        // Use Calendar class to manipulate the dates
        val cal = Calendar.getInstance()
        cal.time = SimpleDateFormat("MMM-dd-yyyy").parse(selectedDate) as Date
        val selectedDay = cal.get(Calendar.DAY_OF_YEAR)
        cal.time = SimpleDateFormat("MMM-dd-yyyy").parse(today) as Date
        val currentDay = cal.get(Calendar.DAY_OF_YEAR)

        // Calculate the difference in days between selected date and current date

        // Show "Yesterday" or "Tomorrow" if the difference is +/- 1
        when (selectedDay - currentDay) {
            -1 -> calendarTv.text = "Yesterday"
            0 -> calendarTv.text = "Today"
            1 -> calendarTv.text = "Tomorrow"
            else -> {
                // For any other case, show the selected date
                calendarTv.text = selectedDate.makeDateReadable()
            }
        }
    }

    companion object {
        /** Convenience method used to check if all permissions required by this app are granted */
        fun hasPermissions(context: Context) = PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            navigateToCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    override fun onPause() {
        super.onPause()

        // This makes sure that if user destroys the app and comes back to it, they have to go thru the sign up process all over again in order to ensure there is no input being missed
        val sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.apply()
    }
}

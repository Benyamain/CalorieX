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
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


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
    private lateinit var calendarTv: TextView
    private lateinit var dashboardFoodLogs: TextView
    private lateinit var dashboardWeightLogs: TextView
    private lateinit var imageIv: ImageView
    private lateinit var dashboardFoodRecyclerView: RecyclerView
    private lateinit var dashboardWeightRecyclerView: RecyclerView
    private var appearBottomCounter = 0
    private var caloriesConsumed: Float = 0f
    private var proteinConsumed: Float = 0f
    private var carbsConsumed: Float = 0f
    private var fatConsumed: Float = 0f


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission request granted", Toast.LENGTH_LONG)
                    .show()
                navigateToCamera()
            } else {
                Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_LONG)
                    .show()
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
        dashboardFoodLogs = view.findViewById(R.id.dashboard_food_logs)
        dashboardWeightLogs = view.findViewById(R.id.dashboard_weight_logs)
        imageIv = view.findViewById(R.id.calendar_image_view)

        navController = findNavController()
        bottomNavigationView.setupWithNavController(navController)
        appearBottomNavigationView.setupWithNavController(navController)

        dashboardFoodRecyclerView = view.findViewById(R.id.dashboard_food_recycler_view)
        dashboardFoodRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        dashboardWeightRecyclerView = view.findViewById(R.id.dashboard_weight_recycler_view)
        dashboardWeightRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadDashboardFoodData()

        imageIv.setOnClickListener {
            navController.navigate(R.id.action_dashboardFragment_to_calendarFragment)
            appearBottomCounter = 0
        }

        dashboardFoodLogs.setOnClickListener {
            loadDashboardFoodData()
        }

        dashboardWeightLogs.setOnClickListener {
            loadDashboardWeightData()
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
                        bottomNavigationView.itemIconTintList =
                            AppCompatResources.getColorStateList(
                                requireContext(),
                                R.color.menu_selector_icon
                            )
                        appearBottomNavigationView.menu.findItem(R.id.menu_add_weight).isChecked =
                            false
                        appearBottomNavigationView.menu.findItem(R.id.menu_snap_food).isChecked =
                            false
                        appearBottomNavigationView.menu.findItem(R.id.menu_add_food).isChecked =
                            false
                        appearBottomNavigationView.menu.findItem(R.id.menu_not_visible).isChecked =
                            false
                        appearBottomCounter = 0
                    }
                    true
                }

                R.id.menu_plus -> {
                    bottomNavigationView.menu.findItem(R.id.menu_plus)
                        .setIcon(R.drawable.ic_plus_foreground)

                    appearBottomCounter++
                    if (appearBottomCounter == 1) {
                        lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                activity?.runOnUiThread {
                                    appearBottom()
                                }
                            }
                        }
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

        return view
    }

    private fun getCalories(callback: (Int) -> Unit) {
        var calories = 0
        Firebase.database.getReference("/${userEmail?.let { encodeEmail(it) }}/energy/energyExpenditure")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        calories = if (dataSnapshot.value is Long) {
                            CalorieAmount(
                                dataSnapshot.getValue(Long::class.java)?.toInt()
                            ).calories ?: 0
                        } else {
                            dataSnapshot.getValue(CalorieAmount::class.java)?.calories ?: 0
                        }
                    }
                    callback(calories)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("databaseError", "$databaseError")
                }
            })
    }

    private fun getMacros(callback: (Triple<Int, Int, Int>) -> Unit) {
        var protein = 0
        var carbs = 0
        var fat = 0
        Firebase.database.getReference("/${userEmail?.let { encodeEmail(it) }}/macros/macroGrams")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    Log.d("CheckingAgain", "${dataSnapshot.getValue(MacroGrams::class.java)}")
                    if (dataSnapshot.exists()) {
                        protein =
                            dataSnapshot.getValue(MacroGrams::class.java)?.proteinGrams ?: 0
                        carbs = dataSnapshot.getValue(MacroGrams::class.java)?.carbGrams ?: 0
                        fat = dataSnapshot.getValue(MacroGrams::class.java)?.fatGrams ?: 0
                        Log.d("Running???", "$dataSnapshot")
                    }
                    callback(Triple(protein, carbs, fat))
                    Log.d("Hmmmmmmmmmmmmmmmmmmmmmm", "${Triple(protein, carbs, fat)}")
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("databaseError", "$databaseError")
                }
            })
    }

    private fun updateCharts() {
        getCalories { calories ->
            val entries: ArrayList<PieEntry> = ArrayList()
            val colors: ArrayList<Int> = ArrayList()
            entries.add(PieEntry(calories.toFloat() - caloriesConsumed))
            entries.add(PieEntry(caloriesConsumed))
            Log.d("getCalories", "$calories")

            colors.add(Color.rgb(162, 165, 171))
            colors.add(Color.rgb(135, 182, 120))

            val dataSet = PieDataSet(entries, "Mobile OS")
            dataSet.setDrawIcons(false)
            dataSet.colors = colors

            val data = PieData(dataSet)
            data.setValueFormatter(object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    if (value.toInt() == 0) return ""
                    else if (value.toInt() < 0) {
                        colors.clear()
                        colors.add(Color.rgb(135, 182, 120))
                        return ""
                    } else return "${value.toInt()} kcal"
                }
            })

            if (entries.isNotEmpty()) {
                data.setValueTextColor(Color.WHITE)
                setupPieChart(caloriePieChart, data)
            } else {
                Toast.makeText(requireContext(), ":)", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        getMacros { macros ->
            val proteinEntries: ArrayList<PieEntry> = ArrayList()
            val carbsEntries: ArrayList<PieEntry> = ArrayList()
            val fatEntries: ArrayList<PieEntry> = ArrayList()
            val proteinColors: ArrayList<Int> = ArrayList()
            val carbsColors: ArrayList<Int> = ArrayList()
            val fatColors: ArrayList<Int> = ArrayList()

            proteinEntries.add(PieEntry(macros.first.toFloat() - proteinConsumed))
            proteinEntries.add(PieEntry(proteinConsumed))
            carbsEntries.add(PieEntry(macros.second.toFloat() - carbsConsumed))
            carbsEntries.add(PieEntry(carbsConsumed))
            fatEntries.add(PieEntry(macros.third.toFloat() - fatConsumed))
            fatEntries.add(PieEntry(fatConsumed))
            Log.d("getProtein", "${macros.first}")

            proteinColors.add(Color.rgb(162, 165, 171))
            proteinColors.add(Color.rgb(135, 182, 120))
            carbsColors.add(Color.rgb(162, 165, 171))
            carbsColors.add(Color.rgb(135, 182, 120))
            fatColors.add(Color.rgb(162, 165, 171))
            fatColors.add(Color.rgb(135, 182, 120))

            val proteinData = PieDataSet(proteinEntries, "Protein")
            val carbsData = PieDataSet(carbsEntries, "Carbs")
            val fatsData = PieDataSet(fatEntries, "Fat")

            proteinData.setDrawIcons(false)
            carbsData.setDrawIcons(false)
            proteinData.setDrawIcons(false)

            proteinData.colors = proteinColors
            carbsData.colors = carbsColors
            fatsData.colors = fatColors

            val pData = PieData(proteinData)
            pData.setValueFormatter(object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    if (value.toInt() == 0) return ""
                    else if (value.toInt() < 0) {
                        proteinColors.clear()
                        proteinColors.add(Color.rgb(135, 182, 120))
                        return ""
                    } else return "${value.toInt()} g"
                }
            })
            val cData = PieData(carbsData)
            cData.setValueFormatter(object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    if (value.toInt() == 0) return ""
                    else if (value.toInt() < 0) {
                        carbsColors.clear()
                        carbsColors.add(Color.rgb(135, 182, 120))
                        return ""
                    } else return "${value.toInt()} g"
                }
            })
            val fData = PieData(fatsData)
            fData.setValueFormatter(object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    if (value.toInt() == 0) return ""
                    else if (value.toInt() < 0) {
                        fatColors.clear()
                        fatColors.add(Color.rgb(135, 182, 120))
                        return ""
                    } else return "${value.toInt()} g"
                }
            })

            pData.setValueTextColor(Color.WHITE)
            cData.setValueTextColor(Color.WHITE)
            fData.setValueTextColor(Color.WHITE)

            setupPieChart(proteinPieChart, pData)
            setupPieChart(carbsPieChart, cData)
            setupPieChart(fatPieChart, fData)
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Do nothing
        }

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                updateCharts()
                readDate()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                updateCharts()
            }
        }

        // Read the date when the user navigates back to the DashboardFragment from the CalendarFragment
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                readDate()
            }
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
                    appearBottomNavigationView.menu.findItem(R.id.menu_not_visible).isChecked =
                        false
                    bottomNavigationView.itemIconTintList =
                        AppCompatResources.getColorStateList(requireContext(), R.color.gray)
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
                    appearBottomNavigationView.menu.findItem(R.id.menu_not_visible).isChecked =
                        false
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
                    appearBottomNavigationView.menu.findItem(R.id.menu_not_visible).isChecked =
                        false
                    true
                }
                // Add more destinations here...
                else -> false
            }
        }
    }

    private fun setupPieChart(pieChart: PieChart, data: PieData) {
        pieChart.setUsePercentValues(false)
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
        data.setValueTextSize(10f)
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
        var date = ""
        Firebase.database.getReference("/${userEmail?.let { email -> encodeEmail(email) }}/calendarDate")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        date = dataSnapshot.getValue(CalendarDate::class.java)?.date ?: ""

                        val today = getCurrentDayString()

                        // Use Calendar class to manipulate the dates
                        val cal = Calendar.getInstance()
                        cal.time = SimpleDateFormat("MMM-dd-yyyy").parse(date) as Date
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
                                calendarTv.text = date.makeDateReadable()
                            }
                        }
                    } else {
                        val setDay = CalendarDate(date = getToday())
                        Firebase.database.getReference(
                            "/${
                                userEmail?.let {
                                    encodeEmail(
                                        it
                                    )
                                }
                            }/calendarDate/date"
                        ).setValue(setDay.date)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("databaseError", "$databaseError")
                }
            })
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

    private fun loadDashboardWeightData() {
        dashboardFoodRecyclerView.visibility = View.INVISIBLE
        dashboardWeightRecyclerView.visibility = View.VISIBLE

        val dashboardWeightData = ArrayList<DashboardWeight>()

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                Firebase.database.getReference("/${userEmail?.let { email -> encodeEmail(email) }}/profileDetails")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                val profileDetails =
                                    dataSnapshot.getValue(ProfileDetails::class.java)
                                val weight = profileDetails?.weight?.get(profileDetails?.weight?.lastIndex ?: 0)?.toInt()

                                val dashboardWeightItem = DashboardWeight(
                                    R.drawable.ic_heart_foreground,
                                    "Weight",
                                    "${weight.toString()} kg" ?: ""
                                )
                                dashboardWeightData.add(dashboardWeightItem)
                            }

                            val weightItems =
                                DashboardWeightItems(
                                    dashboardWeightData,
                                    navController,
                                    appearBottomNavigationView
                                )
                            dashboardWeightRecyclerView.adapter = weightItems
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.d("databaseError", "$databaseError")
                        }
                    })
            }
        }
    }

    private fun loadDashboardFoodData() {
        dashboardWeightRecyclerView.visibility = View.INVISIBLE
        dashboardFoodRecyclerView.visibility = View.VISIBLE

        val dashboardFoodData = ArrayList<DashboardFood>()

        Firebase.database.getReference("/${userEmail?.let { email -> encodeEmail(email) }}/calendarDate")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val date = if (dataSnapshot.value is Long) {
                            CalendarDate(
                                dataSnapshot.getValue(
                                    Long::class.java
                                )
                                    ?.toString()
                            )
                        } else {
                            dataSnapshot.getValue(CalendarDate::class.java)
                        }


                        Firebase.database.getReference(
                            "/${
                                userEmail?.let { email ->
                                    encodeEmail(
                                        email
                                    )
                                }
                            }/calendarDate/${date?.date}/dashboard/foodSelection"
                        )
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    dashboardFoodData.clear()

                                    for (childSnapshot in dataSnapshot.children) {
                                        val foodItem =
                                            if (childSnapshot.value is Long) {
                                                FoodItem(
                                                    childSnapshot.getValue(
                                                        Long::class.java
                                                    )?.toString()
                                                )
                                            } else {
                                                childSnapshot.getValue(FoodItem::class.java)
                                            }

                                        val dashboardItem = DashboardFood(
                                            foodItem?.image ?: "",
                                            foodItem?.name ?: "",
                                            "${(foodItem?.weight ?: 0)} g",
                                            "${foodItem?.calorie} kcal" ?: ""
                                        )
                                        dashboardFoodData.add(dashboardItem)
                                        caloriesConsumed += (foodItem?.calorie ?: "").toFloat()
                                        proteinConsumed += (foodItem?.protein ?: "").toFloat()
                                        Log.d("proteinConsumed", "$proteinConsumed")
                                        carbsConsumed += (foodItem?.carbs ?: "").toFloat()
                                        fatConsumed += (foodItem?.fat ?: "").toFloat()
                                    }

                                    // Create the DashboardItemsAdapter with the dashboardData list
                                    val dashboardFoodItems =
                                        DashboardFoodItems(
                                            dashboardFoodData,
                                            navController,
                                            appearBottomNavigationView
                                        )
                                    dashboardFoodRecyclerView.adapter = dashboardFoodItems
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Handle database error
                                }
                            })
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.d("databaseError", "$databaseError")
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()

        // This makes sure that if user destroys the app and comes back to it, they have to go thru the sign up process all over again in order to ensure there is no input being missed
        val sharedPreferences =
            requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.apply()
    }
}

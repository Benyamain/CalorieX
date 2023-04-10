package com.example.caloriex

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mancj.materialsearchbar.MaterialSearchBar

class SearchFoodFragment : Fragment(), MaterialSearchBar.OnSearchActionListener {

    private lateinit var navController: NavController
    private lateinit var searchBar: MaterialSearchBar
    private lateinit var searchResultsRecyclerView: RecyclerView
    private lateinit var searchResultsAdapter: FoodListAdapter
    private var lastSearches: MutableList<String> = mutableListOf()
    private val PREFS_NAME = "search_history"
    private val PREFS_KEY_SEARCHES = "searches"
    private lateinit var foodVm: FoodViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_food, container, false)
        navController = findNavController()
        searchBar = view.findViewById(R.id.material_search_bar)
        searchBar.setOnSearchActionListener(this)
        lastSearches = loadSearchSuggestionFromDisk().toMutableList()
        searchBar.lastSuggestions = lastSearches
        foodVm = ViewModelProvider(this)[FoodViewModel::class.java]

        searchResultsRecyclerView = view.findViewById(R.id.search_food_recycler_view)
        searchResultsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        searchResultsAdapter = FoodListAdapter(arrayListOf(),navController)
        searchResultsRecyclerView.adapter = searchResultsAdapter

        foodVm.foodsData.observe(viewLifecycleOwner) { foodItems ->
            searchResultsAdapter.foodsList.clear()
            searchResultsAdapter.foodsList.addAll(foodItems)
            searchResultsAdapter.notifyDataSetChanged()
        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navController.navigate(R.id.dashboardFragment)
        }
    }

    override fun onSearchStateChanged(enabled: Boolean) {

    }

    override fun onSearchConfirmed(text: CharSequence?) {
        text?.let {
            // Add search term to search history
            lastSearches.add(it.toString())

            // Save search history to disk
            saveSearchSuggestionsToDisk(lastSearches)

            // Do something with search term
            foodVm.getData(it.toString())
        }
    }

    override fun onButtonClicked(buttonCode: Int) {
        when (buttonCode) {
            MaterialSearchBar.BUTTON_BACK -> {
                // Handle navigation button click
            }
        }
    }

    private fun loadSearchSuggestionFromDisk(): List<String> {
        val prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(PREFS_KEY_SEARCHES, setOf())?.toList() ?: emptyList()
    }

    private fun saveSearchSuggestionsToDisk(searchHistory: List<String>) {
        val prefs = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putStringSet(PREFS_KEY_SEARCHES, searchHistory.toSet()).apply()
    }
}

package com.example.caloriex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.mancj.materialsearchbar.MaterialSearchBar

class SearchFoodFragment : Fragment(), MaterialSearchBar.OnSearchActionListener {

    private lateinit var navController: NavController
    private lateinit var searchBar: MaterialSearchBar
    private lateinit var lastSearches: List<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_food, container, false)
        navController = findNavController()
        searchBar = view.findViewById(R.id.material_search_bar)
        searchBar.setOnSearchActionListener(this)

        lastSearches = loadSearchSuggestionFromDisk()
        searchBar.lastSuggestions = lastSearches

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            //navController.popBackStack()
            // Add animation for slide up and down
        }
    }

    override fun onSearchStateChanged(enabled: Boolean) {
        val s = if (enabled) "enabled" else "disabled"
        Toast.makeText(requireContext(), "Search $s", Toast.LENGTH_SHORT).show()
    }

    override fun onSearchConfirmed(text: CharSequence?) {
        TODO("Not yet implemented")
    }

    override fun onButtonClicked(buttonCode: Int) {
        when (buttonCode) {
            MaterialSearchBar.BUTTON_BACK -> {
                // Handle navigation button click
            }
        }
    }

    private fun loadSearchSuggestionFromDisk(): List<String> {
        // Implement loading from disk
        return emptyList()
    }
}

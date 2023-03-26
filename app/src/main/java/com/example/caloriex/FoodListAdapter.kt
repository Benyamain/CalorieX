package com.example.caloriex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView

class FoodListAdapter(
    private val list: List<FoodListItems>,
    private val navController: NavController
) : RecyclerView.Adapter<FoodListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.food_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = list[position]
        holder.textView.text = itemsViewModel.foodText
        holder.itemView.setOnClickListener {
            navController.navigate(R.id.action_searchFoodFragment_to_nutritionInfoFragment)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.food_list_item_text_view)
    }
}

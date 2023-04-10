package com.example.caloriex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView

const val CALORIE = "TotalCalories"
const val NAME = "FoodName"

class FoodListAdapter(
    val foodsList: ArrayList<FoodApiModel>,
    private val navController: NavController
) : RecyclerView.Adapter<FoodListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.food_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentItem = foodsList[position].hints.firstOrNull()?.food
        holder.textView.text = currentItem?.label.toString()
        holder.itemView.setOnClickListener {
            navController.navigate(R.id.action_searchFoodFragment_to_nutritionInfoFragment)
        }
    }

    override fun getItemCount(): Int {
        return foodsList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.food_list_item_text_view)
    }
}

package com.example.caloriex

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

const val CALORIE = "calorie"
const val NAME = "name"
const val SUGAR = "sugar"
const val CARBS = "carbs"
const val PROTEIN = "protein"
const val MONOFAT = "monofat"
const val POLYFAT = "polyfat"
const val FIBER = "fiber"
const val SATFAT = "satfat"
const val FAT = "fat"

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
        holder.textView.text = currentItem?.label?.toString() ?: "Not Available!"
        Picasso.get().load(currentItem?.image).into(holder.leftIv)
        if (  holder.textView.text != "Not Available!") {
            holder.itemView.setOnClickListener {
                val bundle = Bundle().apply {
                    putString(NAME, currentItem?.label.toString())
                    putString(CALORIE, currentItem?.nutrients?.ENERC_KCAL.toString())
                    putString(SUGAR, currentItem?.nutrients?.SUGAR.toString())
                    putString(CARBS, currentItem?.nutrients?.CHOCDF.toString())
                    putString(PROTEIN, currentItem?.nutrients?.PROCNT.toString())
                    putString(MONOFAT, currentItem?.nutrients?.FAMS.toString())
                    putString(POLYFAT, currentItem?.nutrients?.FAPU.toString())
                    putString(FIBER, currentItem?.nutrients?.FIBTG.toString())
                    putString(SATFAT, currentItem?.nutrients?.FASAT.toString())
                    putString(FAT, currentItem?.nutrients?.FAT.toString())
                }

                navController.navigate(
                    R.id.action_searchFoodFragment_to_nutritionInfoFragment,
                    bundle
                )
            }
        } else {
            Toast.makeText(
                holder.itemView.context,
                "Invalid Food Search!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun getItemCount(): Int {
        return foodsList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.food_list_item_text_view)
        val leftIv: ImageView = itemView.findViewById(R.id.food_list_left_image_view)
    }
}

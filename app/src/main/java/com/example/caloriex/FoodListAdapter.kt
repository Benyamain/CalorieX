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
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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
                val foodItem = FoodItem(
                    name = currentItem?.label.toString(),
                    calorie = currentItem?.nutrients?.ENERC_KCAL.toString(),
                    sugar = currentItem?.nutrients?.SUGAR.toString(),
                    carbs = currentItem?.nutrients?.CHOCDF.toString(),
                    protein = currentItem?.nutrients?.PROCNT.toString(),
                    monofat = currentItem?.nutrients?.FAMS.toString(),
                    polyfat = currentItem?.nutrients?.FAPU.toString(),
                    fiber = currentItem?.nutrients?.FIBTG.toString(),
                    satfat = currentItem?.nutrients?.FASAT.toString(),
                    fat = currentItem?.nutrients?.FAT.toString(),
                    image = currentItem?.image.toString()
                )

                if (userEmail != null) {
                    val key = Firebase.database.reference.child("foodSelection").child(encodeEmail(userEmail)).push().key?: ""
                    Firebase.database.reference.child("foodSelection").child(encodeEmail(userEmail)).child(key).setValue(foodItem)
                    val foodItemKey = FoodItemKey(key = key)
                    Firebase.database.reference.child("foodSelectionKeys").child(encodeEmail(userEmail)).setValue(foodItemKey)
                }

                navController.navigate(
                    R.id.action_searchFoodFragment_to_nutritionInfoFragment)
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

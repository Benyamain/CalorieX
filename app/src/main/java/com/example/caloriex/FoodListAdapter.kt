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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

                foodItems.add(foodItem)

                if (userEmail != null) {

                    Firebase.database.getReference("/${encodeEmail(userEmail)}/calendarDate")
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

                                    val key = Firebase.database.getReference("/${encodeEmail(userEmail)}/calendarDate/${date?.date}/foodSelection").push().key?: ""
                                    Firebase.database.getReference("/${encodeEmail(userEmail)}/calendarDate/${date?.date}/foodSelection/$key").setValue(foodItem)
                                    val fKey = FoodItemKey(key = key)
                                    foodItemKey.add(fKey)
                                    Firebase.database.getReference("/${encodeEmail(userEmail)}/calendarDate/${date?.date}/foodSelectionKeys").setValue(fKey)
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                Log.d("databaseError", "$databaseError")
                            }
                        })
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

package com.example.caloriex

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private val list: List<ItemsViewModel>, private val navController: NavController) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = list[position]
        holder.textView.text = itemsViewModel.text
        // sets the image to the imageview from our itemHolder class
        holder.leftImageView.setImageResource(itemsViewModel.leftImage)
        holder.rightImageView.setImageResource(itemsViewModel.rightImage)
        holder.itemView.setOnClickListener {
            when (position) {
                0 -> navController.navigate(R.id.action_settingsFragment_to_updateProfileDetailsFragment)
                1 -> navController.navigate(R.id.action_settingsFragment_to_updateEnergySettingsFragment)
                2 -> navController.navigate(R.id.action_settingsFragment_to_updateMacroRatiosFragment)
                3 -> navController.navigate(R.id.action_settingsFragment_to_logoutFragment)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.list_item_text_view)
        val rightImageView: ImageView = itemView.findViewById(R.id.right_image_view)
        val leftImageView: ImageView = itemView.findViewById(R.id.left_image_view)
    }
}

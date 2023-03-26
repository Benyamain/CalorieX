package com.example.caloriex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardItemsAdapter(
    private val list: List<DashboardItems>,
    private val navController: NavController,
    private var appearBottomNavigationView: BottomNavigationView
) : RecyclerView.Adapter<DashboardItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dashboard_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = list[position]
        holder.centerTextView.text = itemsViewModel.centerText
        holder.rightTextView.text = itemsViewModel.rightText
        holder.belowTextView.text = itemsViewModel.belowText
        holder.belowRightTextView.text = itemsViewModel.belowRightText
        // sets the image to the imageview from our itemHolder class
        holder.leftImageView.setImageResource(itemsViewModel.leftImage)

        holder.itemView.setOnClickListener {
            if (appearBottomNavigationView.visibility != View.VISIBLE) {
                navController.navigate(R.id.action_dashboardFragment_to_nutritionInfoFragment)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val centerTextView: TextView = itemView.findViewById(R.id.dashboard_center_text_view)
        val belowTextView: TextView = itemView.findViewById(R.id.dashboard_below_text_view)
        val rightTextView: TextView = itemView.findViewById(R.id.dashboard_right_text_view)
        val belowRightTextView: TextView = itemView.findViewById(R.id.dashboard_below_right_text_view)
        val leftImageView: ImageView = itemView.findViewById(R.id.dashboard_left_image_view)
    }
}

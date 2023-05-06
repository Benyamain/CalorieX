package com.example.caloriex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardWeightAdapter(
    private val list: List<DashboardWeight>,
    private val navController: NavController,
    private var appearBottomNavigationView: BottomNavigationView
) : RecyclerView.Adapter<DashboardWeightAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dashboard_weight, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = list[position]
        holder.centerTv?.text = itemsViewModel.centerText
        holder.rightTv?.text = itemsViewModel.rightText
        holder.leftIv?.setImageResource(itemsViewModel.leftImage)
        holder.itemView.setOnClickListener {
            if (appearBottomNavigationView.visibility != View.VISIBLE) {
                navController.navigate(R.id.action_dashboardFragment_to_addWeightFragment)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var centerTv: TextView? = null
        var rightTv: TextView? = null
        var leftIv: ImageView? = null
        init {
            centerTv = itemView.findViewById(R.id.weight_dashboard_center_text_view)
            rightTv = itemView.findViewById(R.id.weight_dashboard_right_text_view)
            leftIv = itemView.findViewById(R.id.weight_dashboard_left_image_view)
        }
    }
}
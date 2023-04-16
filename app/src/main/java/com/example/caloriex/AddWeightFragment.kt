package com.example.caloriex

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AddWeightFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var imageIv: ImageView
    private lateinit var addWeightTv: TextView
    private lateinit var saveBtn: Button
    private lateinit var weightEt: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_weight, container, false)
        toolbar = view.findViewById(R.id.appear_bottom_toolbar)
        navController = findNavController()
        imageIv = view.findViewById(R.id.appear_bottom_toolbar_close_image_view)
        addWeightTv = view.findViewById(R.id.appear_bottom_toolbar_title_textview)
        addWeightTv.text = "Weight"
        weightEt = view.findViewById(R.id.add_weight_amount_edittext)

        imageIv.setOnClickListener {
            navController.navigate(R.id.dashboardFragment)
        }

        saveBtn = view.findViewById(R.id.add_weight_save_button)
        saveBtn.setOnClickListener {
            intentToDashboard()
        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            // Do nothing
        }

        userEmail?.let { encodeEmail(it) }?.let {
            Firebase.database.getReference("profileDetails")
                .child(it)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val profileDetails = dataSnapshot.getValue(ProfileDetails::class.java)
                            weightEt.setText(profileDetails?.weight.toString())
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.d("databaseError", "$databaseError")
                    }
                })
        }
    }

    private fun intentToDashboard() {
        // val bundle = Bundle()
        //  bundle.putString("date", newDate)

        navController.navigate(R.id.dashboardFragment)

    }

    override fun onResume() {
        super.onResume()
    }
}

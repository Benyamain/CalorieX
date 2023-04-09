package com.example.caloriex

data class EnergySettings(val bmrName: String? = null, val activityLevel: String? = null, val weightGoal: Double? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}

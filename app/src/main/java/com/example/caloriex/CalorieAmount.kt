package com.example.caloriex

data class CalorieAmount(val calories: Int? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}

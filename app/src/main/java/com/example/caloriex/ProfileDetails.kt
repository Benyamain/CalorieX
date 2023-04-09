package com.example.caloriex

data class ProfileDetails(val age: Int? = null, val height: Double? = null, val weight: Double? = null, val sex: String? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}

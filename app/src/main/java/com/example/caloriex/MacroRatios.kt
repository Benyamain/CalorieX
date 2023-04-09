package com.example.caloriex

data class MacroRatios(val proteinRatio: Double? = null, val netCarbRatio: Double? = null, val fatRatio: Double? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}

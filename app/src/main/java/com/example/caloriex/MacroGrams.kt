package com.example.caloriex

data class MacroGrams(val proteinGrams: Int? = null, val carbGrams: Int? = null, val fatGrams: Int? = null) {
    // Null default values create a no-argument default constructor, which is needed
    // for deserialization from a DataSnapshot.
}

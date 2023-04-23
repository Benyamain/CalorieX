package com.example.caloriex

data class FoodItem(
    val name: String? = null,
    val calorie: String? = null,
    val sugar: String? = null,
    val carbs: String? = null,
    val protein: String? = null,
    val monofat: String? = null,
    val polyfat: String? = null,
    val fiber: String? = null,
    val satfat: String? = null,
    val fat: String? = null,
    val image: String? = null,
) {
    // No argument constructor is needed whenever dealing with Firebase Realtime Database
}

val foodItems: ArrayList<FoodItem> = ArrayList()


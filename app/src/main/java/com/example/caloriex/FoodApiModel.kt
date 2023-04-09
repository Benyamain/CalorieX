package com.example.caloriex

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FoodApiModel(
    @SerializedName("parsed")
    val hints: List<Hint>,

    @SerializedName("text")
    val searchText: String? = null
): Parcelable

package com.example.caloriex

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Food(
    @SerializedName("category")
    val category: String? = null,

    @SerializedName("image")
    val image: String? = null,

    @SerializedName("label")
    val label: String? = null,

    @SerializedName("nutrients")
    val nutrients: Nutrients
) : Parcelable

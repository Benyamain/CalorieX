package com.example.caloriex

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Hint(
    @SerializedName("food")
    val food: Food
): Parcelable
package com.example.caloriex

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Nutrients(
    @SerializedName("CHOCDF")
    val CHOCDF: Double? = null,

    @SerializedName("ENERC_KCAL")
    val ENERC_KCAL: Int? = null,

    @SerializedName("FAT")
    val FAT: Double? = null,

    @SerializedName("FIBTG")
    val FIBTG: Double? = null,

    @SerializedName("PROCNT")
    val PROCNT: Double? = null
) : Parcelable

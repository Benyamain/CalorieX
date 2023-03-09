package com.example.caloriex

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Month
import java.util.*

@SuppressLint("SimpleDateFormat")
fun getCurrentDayString(): String {
    val date = Date();
    val formatter = SimpleDateFormat("MMM-dd-yyyy")
    return formatter.format(date)
}

@RequiresApi(Build.VERSION_CODES.O)
fun constructDate(year: Int, month: Int, dayOfMonth: Int): String = "${getMonthString(month)}.-$dayOfMonth-$year"

@RequiresApi(Build.VERSION_CODES.O)
fun getMonthString(month: Int): String = when (month) {
    1 -> Month.JANUARY.name.lowercase(Locale.getDefault())
    2 -> Month.FEBRUARY.name.lowercase(Locale.getDefault())
    3 -> Month.MARCH.name.lowercase(Locale.getDefault())
    4 -> Month.APRIL.name.lowercase(Locale.getDefault())
    5 -> Month.MAY.name.lowercase(Locale.getDefault())
    6 -> Month.JUNE.name.lowercase(Locale.getDefault())
    7 -> Month.JULY.name.lowercase(Locale.getDefault())
    8 -> Month.AUGUST.name.lowercase(Locale.getDefault())
    9 -> Month.SEPTEMBER.name.lowercase(Locale.getDefault())
    10 -> Month.OCTOBER.name.lowercase(Locale.getDefault())
    11 -> Month.NOVEMBER.name.lowercase(Locale.getDefault())
    12 -> Month.DECEMBER.name.lowercase(Locale.getDefault())
    else -> ""
}
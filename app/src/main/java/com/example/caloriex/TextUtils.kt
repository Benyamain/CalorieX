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
fun constructDate(year: Int, month: Int, dayOfMonth: Int): String = "${getMonthString(month)}-$dayOfMonth-$year"

@RequiresApi(Build.VERSION_CODES.O)
fun getMonthString(month: Int): String = when (month) {
    1 -> Month.JANUARY.name.lowercase()
    2 -> Month.FEBRUARY.name.lowercase()
    3 -> Month.MARCH.name.lowercase()
    4 -> Month.APRIL.name.lowercase()
    5 -> Month.MAY.name.lowercase()
    6 -> Month.JUNE.name.lowercase()
    7 -> Month.JULY.name.lowercase()
    8 -> Month.AUGUST.name.lowercase()
    9 -> Month.SEPTEMBER.name.lowercase()
    10 -> Month.OCTOBER.name.lowercase()
    11 -> Month.NOVEMBER.name.lowercase()
    12 -> Month.DECEMBER.name.lowercase()
    else -> ""
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.makeDateReadable(): String {
    val strArr = this.split("-")
    val sb = StringBuilder()

    val monthDB = strArr[0]
    sb.append(monthDB.monthToMonthComplete().firstCharToUpper()).append(" ").append(strArr[1]).append(", " + strArr[2])
    return sb.toString()
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.monthToMonthComplete(): String = when(this) {
    Month.JANUARY.name.lowercase() -> MonthComplete.JANUARY.name.lowercase()
    Month.FEBRUARY.name.lowercase() -> MonthComplete.FEBRUARY.name.lowercase()
    Month.MARCH.name.lowercase()-> MonthComplete.MARCH.name.lowercase()
    Month.APRIL.name.lowercase() -> MonthComplete.APRIL.name.lowercase()
    Month.MAY.name.lowercase() -> MonthComplete.MAY.name.lowercase()
    Month.JUNE.name.lowercase() -> MonthComplete.JUNE.name.lowercase()
    Month.JULY.name.lowercase() -> MonthComplete.JULY.name.lowercase()
    Month.AUGUST.name.lowercase() -> MonthComplete.AUGUST.name.lowercase()
    Month.SEPTEMBER.name.lowercase() -> MonthComplete.SEPTEMBER.name.lowercase()
    Month.OCTOBER.name.lowercase() -> MonthComplete.OCTOBER.name.lowercase()
    Month.NOVEMBER.name.lowercase() -> MonthComplete.NOVEMBER.name.lowercase()
    Month.DECEMBER.name.lowercase() -> MonthComplete.DECEMBER.name.lowercase()
    else -> ""
}

enum class MonthComplete {
    JANUARY,
    FEBRUARY,
    MARCH,
    APRIL,
    MAY,
    JUNE,
    JULY,
    AUGUST,
    SEPTEMBER,
    OCTOBER,
    NOVEMBER,
    DECEMBER
}

fun String.firstCharToUpper() = this[0].toUpperCase() + this.substring(1, this.length)

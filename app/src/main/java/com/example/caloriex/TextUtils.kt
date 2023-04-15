package com.example.caloriex

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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

fun calculateBMRMifflinStJeor(gender: String, weight: Double, height: Double, age: Int, activityLevel: String, weightGoal: Double?): Int {
    val s = if (gender.equals("Female", ignoreCase = true)) 5 else -161
    val bmr = (10 * weight) + (6.25 * height) - (5 * age) + s
    val ratio = weightGoal?.div(weight) ?: 1.0
    val adjustedBMR = bmr * ratio

    // Adjust for activity level
    when (activityLevel) {
        "Sedentary" -> return (adjustedBMR * 1.2).toInt()
        "Lightly Active" -> return (adjustedBMR * 1.375).toInt()
        "Moderately Active" -> return (adjustedBMR * 1.55).toInt()
        "Very Active" -> return (adjustedBMR * 1.725).toInt()
        "Extremely Active" -> return (adjustedBMR * 1.9).toInt()
        else -> return adjustedBMR.toInt()
    }
}

fun calculateBMRHarrisBenedict(gender: String, weight: Double, height: Double, age: Int, activityLevel: String, weightGoal: Double?): Int {
    val s = if (gender.equals("Male", ignoreCase = true)) 88.362 else 447.593
    val bmr = s + (13.397 * weight) + (4.799 * height) - (5.677 * age)
    val ratio = weightGoal?.div(weight) ?: 1.0
    val adjustedBMR = bmr * ratio

    // Adjust for activity level
    when (activityLevel) {
        "Sedentary" -> return (adjustedBMR * 1.2).toInt()
        "Lightly Active" -> return (adjustedBMR * 1.375).toInt()
        "Moderately Active" -> return (adjustedBMR * 1.55).toInt()
        "Very Active" -> return (adjustedBMR * 1.725).toInt()
        "Extremely Active" -> return (adjustedBMR * 1.9).toInt()
        else -> return adjustedBMR.toInt()
    }
}

fun calculateMacronutrientRatios(bmr: Int, proteinRatio: Double, netCarbRatio: Double, fatRatio: Double): Triple<Int, Int, Int> {
    val totalRatio = proteinRatio + netCarbRatio + fatRatio
    val proteinIntake = ((proteinRatio / totalRatio) * bmr).toInt()
    val netCarbIntake = ((netCarbRatio / totalRatio) * bmr).toInt()
    val fatIntake = ((fatRatio / totalRatio) * bmr).toInt()
    return Triple(proteinIntake, netCarbIntake, fatIntake)
}

fun creatingProfile(age: Int, height: Double, weight: Double, sex: String) {
    val profile = ProfileDetails(age, height, weight, sex)

    if (userEmail != null) {
        Firebase.database.reference.child("profileDetails").child(encodeEmail(userEmail)).setValue(profile)
    }
}

fun encodeEmail(email: String): String {
    return email.replace(".", ",").replace("@", "_")
}


fun energySettings(bmrName: String, activityLevel: String, weightGoal: Double) {
    val energy = EnergySettings(bmrName, activityLevel, weightGoal)

    if (userEmail != null) {
        Firebase.database.reference.child("energySettings").child(encodeEmail(userEmail)).setValue(energy)
    }
}

fun macroRatios(protein: Double, netCarb: Double, fat: Double) {
    val macros = MacroRatios(protein, netCarb, fat)

    if (userEmail != null) {
        Firebase.database.reference.child("macroRatios").child(encodeEmail(userEmail)).setValue(macros)
    }
}

val auth = FirebaseAuth.getInstance()
val userEmail = auth.currentUser?.email







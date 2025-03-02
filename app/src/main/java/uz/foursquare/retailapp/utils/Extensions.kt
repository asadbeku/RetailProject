package uz.foursquare.retailapp.utils

import android.icu.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.convertToPriceFormat(): String {
    return try {
        val formatted = NumberFormat.getNumberInstance(Locale.US)
            .apply { isGroupingUsed = true }
            .format(this)
            .replace(",", " ")
        "$formatted UZS"
    } catch (e: Exception) {
        "0 UZS"
    }
}

fun Long.millisToDate(): String {
    val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return sdf.format(Date(this))
}

fun Long.millisToTime(): String {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return sdf.format(Date(this))
}
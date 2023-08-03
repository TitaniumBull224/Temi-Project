package com.ibsystem.temiassistant.utils

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Locale

fun Context.showToastShort(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun reformatDate(time: String): String {
    val dateTimeString = time.substring(0, 23)
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
    val parsedDateTime = parser.parse(dateTimeString)
    val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return formatter.format(parsedDateTime!!)
}
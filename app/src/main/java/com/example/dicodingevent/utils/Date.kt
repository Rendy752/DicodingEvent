package com.example.dicodingevent.utils

import java.text.SimpleDateFormat
import java.util.Locale

object Date {
    fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEEE, d MMMM yyyy HH:mm", Locale("id", "ID"))
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date!!)
    }
}
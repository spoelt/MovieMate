package com.spoelt.moviemate.presentation.util

import android.os.Build
import android.text.format.DateFormat
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

object DateTimeFormatter {
    fun getYearFromDateString(dateString: String?): String? {
        return dateString?.let { date ->
            try {
                LocalDate.parse(date).year.toString()
            } catch (e: DateTimeParseException) {
                null
            }
        }
    }

    fun formatDate(
        pattern: String = "ddMMyyyy",
        locale: Locale = Locale.getDefault(),
        date: String
    ): String {
        val localDate = LocalDate.parse(date)
        val bestDateTimePattern = DateFormat.getBestDateTimePattern(locale, pattern)
        return localDate.format(DateTimeFormatter.ofPattern(bestDateTimePattern, locale))
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun formatRuntime(runtime: Int): String {
        val duration = Duration.ofMinutes(runtime.toLong())
        val hours = duration.toHours()
        val remainingMinutes = duration.toMinutesPart()

        return when {
            hours > 0 && remainingMinutes > 0 -> "${hours}h ${remainingMinutes}m"
            hours > 0 -> "${hours}h"
            else -> "${remainingMinutes}m"
        }
    }
}
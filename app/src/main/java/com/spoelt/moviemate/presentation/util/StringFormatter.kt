package com.spoelt.moviemate.presentation.util

import java.text.NumberFormat
import java.util.Locale

data object StringFormatter {
    fun mapBudget(budget: Int?): String? = budget?.let { b ->
        val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
        formatter.maximumFractionDigits = 0
        formatter.format(b.toLong())
    }

    fun mapRevenue(revenue: Int?): String? = revenue?.let { r ->
        val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
        formatter.maximumFractionDigits = 0
        formatter.format(r.toLong())
    }

    fun mapLanguage(languageCode: String?): String? = languageCode?.let { code ->
        val locale = Locale(code)
        return locale.displayLanguage
    }

    fun mapRating(rating: Float?, reviews: Int?): String? {
        return if (rating != null && reviews != null) {
            String.format(Locale.getDefault(), "%.2f (%d)", rating, reviews)
        } else {
            null
        }
    }
}
package com.farmdirect.app.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {
    fun format(amount: Double, currency: String = "KES"): String {
        val formatter = NumberFormat.getNumberInstance(Locale.US)
        formatter.minimumFractionDigits = 0; formatter.maximumFractionDigits = 0
        return when (currency) { "KES" -> "KES ${formatter.format(amount)}"; "USD" -> "$${formatter.format(amount)}"; else -> "${formatter.format(amount)} $currency" }
    }
    fun formatShort(amount: Double): String = when { amount >= 1_000_000 -> "KES ${"%.1f".format(amount/1_000_000)}M"; amount >= 1_000 -> "KES ${"%.0f".format(amount/1_000)}K"; else -> "KES ${"%.0f".format(amount)}" }
}

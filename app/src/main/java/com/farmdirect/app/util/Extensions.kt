package com.farmdirect.app.util

import java.text.SimpleDateFormat
import java.util.*

fun Long.toDateString(format: String = "MMM dd, yyyy"): String = SimpleDateFormat(format, Locale.getDefault()).format(Date(this))

fun Long.toTimeAgo(): String {
    val diff = System.currentTimeMillis() - this
    return when { diff < 60_000 -> "Just now"; diff < 3_600_000 -> "${diff/60_000}m ago"; diff < 86_400_000 -> "${diff/3_600_000}h ago"; diff < 604_800_000 -> "${diff/86_400_000}d ago"; else -> toDateString() }
}

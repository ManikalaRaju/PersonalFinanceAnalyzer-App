package uk.ac.tees.mad.s3470478.utils

fun getCategoryIcon(category: String): String {
    return when (category) {
        "Food" -> "🍔"
        "Everyday Needs" -> "🧻"
        "Entertainment" -> "🎬"
        "Travel" -> "✈️"
        "Health Care" -> "💊"
        "Shopping" -> "🛍️"
        "Rent" -> "🏠"
        "Others" -> "📦"
        else -> "💰"
    }
}

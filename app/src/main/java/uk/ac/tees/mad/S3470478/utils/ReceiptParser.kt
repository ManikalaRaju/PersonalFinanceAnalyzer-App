package uk.ac.tees.mad.s3470478.utils

fun parseReceiptText(text: String): Triple<Double?, String, String> {
    val lines = text.lines()

    val totalLine = lines.find { it.contains("total", ignoreCase = true) }
    val amountRegex = Regex("""[£\$]?\s?(\d+\.\d{2})""")
    val amount = totalLine?.let { amountRegex.find(it)?.groupValues?.get(1)?.toDoubleOrNull() }

    val categories = mapOf(
        "food" to listOf("restaurant", "food", "meal", "cafe", "burger"),
        "travel" to listOf("uber", "taxi", "fuel", "bus", "train"),
        "shopping" to listOf("mall", "shopping", "store", "clothes"),
        "health" to listOf("pharmacy", "medicine", "hospital"),
        "rent" to listOf("rent"),
        "entertainment" to listOf("movie", "cinema", "game", "netflix")
    )

    var matchedCategory = "📦 Others"
    for ((category, keywords) in categories) {
        if (keywords.any { keyword -> text.contains(keyword, ignoreCase = true) }) {
            matchedCategory = when (category) {
                "food" -> "🍔 Food"
                "travel" -> "✈️ Travel"
                "shopping" -> "🛍 Shopping"
                "health" -> "💊 Health Care"
                "rent" -> "🏠 Rent"
                "entertainment" -> "🎮 Entertainment"
                else -> "📦 Others"
            }
            break
        }
    }

    // Use first line as a note (store name or title)
    val note = lines.firstOrNull()?.take(30) ?: "Receipt"

    return Triple(amount, matchedCategory, note)
}
package uk.ac.tees.mad.s3470478.utils

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

data class ParsedReceipt(
    val amount: String?,
    val category: String?,
    val note: String?
)

object ReceiptAnalyzer {

    fun analyzeReceiptText(
        context: Context,
        imageUri: Uri,
        onResult: (String) -> Unit
    ) {
        try {
            val image = InputImage.fromFilePath(context, imageUri)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.Builder().build())

            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    onResult(visionText.text)
                }
                .addOnFailureListener {
                    onResult("Error: ${it.message}")
                }
        } catch (e: Exception) {
            onResult("Failed to load image: ${e.message}")
        }
    }

    fun parseText(receiptText: String): ParsedReceipt {
        val lines = receiptText.lines().filter { it.isNotBlank() }

        // Detect Total Amount
        val totalLine = lines.firstOrNull { line ->
            line.contains("total", ignoreCase = true) ||
                    line.contains("amount due", ignoreCase = true) ||
                    line.contains("grand total", ignoreCase = true)
        }

        val amountRegex = Regex("""(\d+\.\d{2})""")
        val amount = totalLine?.let { amountRegex.find(it)?.value }
            ?: amountRegex.find(receiptText)?.value

        // Shop Name
        val shopName = lines.firstOrNull()

        //  Detect Category based on keywords
        val lowerText = receiptText.lowercase()
        val category = when {
            listOf("food", "restaurant", "meal", "pizza", "burger").any { it in lowerText } -> "Food"
            listOf("grocery", "mart", "supermarket", "essentials").any { it in lowerText } -> "Everyday Needs"
            listOf("movie", "netflix", "concert", "entertainment").any { it in lowerText } -> "Entertainment"
            listOf("uber", "taxi", "train", "flight", "ticket").any { it in lowerText } -> "Travel"
            listOf("clinic", "medicine", "pharmacy", "doctor").any { it in lowerText } -> "Health Care"
            listOf("shopping", "clothes", "store", "mall").any { it in lowerText } -> "Shopping"
            listOf("rent", "lease", "apartment", "flat").any { it in lowerText } -> "Rent"
            else -> "Others"
        }

        return ParsedReceipt(
            amount = amount,
            category = category,
            note = shopName
        )
    }


}

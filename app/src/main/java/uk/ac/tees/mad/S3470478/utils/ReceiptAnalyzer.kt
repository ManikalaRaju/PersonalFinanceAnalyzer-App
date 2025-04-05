package uk.ac.tees.mad.s3470478.utils

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

object ReceiptAnalyzer {

    fun analyzeReceiptText(context: Context, uri: Uri, onResult: (String) -> Unit) {
        try {
            val image = InputImage.fromFilePath(context, uri)

            val recognizer = TextRecognition.getClient(
                TextRecognizerOptions.Builder().build()
            )

            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    onResult(visionText.text)
                }
                .addOnFailureListener { e ->
                    onResult("Failed to read text: ${e.message}")
                }
        } catch (e: Exception) {
            onResult("Error loading image: ${e.message}")
        }
    }
}

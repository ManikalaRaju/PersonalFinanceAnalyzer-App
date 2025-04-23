package uk.ac.tees.mad.s3470478.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uk.ac.tees.mad.s3470478.model.ExpenseEntity

object ExpenseFirestoreHelper {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private fun getUserExpensesRef() =
        auth.currentUser?.uid?.let { uid ->
            db.collection("users").document(uid).collection("expenses")
        }
    fun uploadExpense(expense: ExpenseEntity, onComplete: (Boolean) -> Unit) {
        val data = hashMapOf(
            "amount" to expense.amount,
            "category" to expense.category,
            "note" to expense.note,
            "timestamp" to expense.timestamp
        )

        val ref = getUserExpensesRef()
        if (ref != null) {
            ref.document(expense.id.toString())
                .set(data)
                .addOnSuccessListener { onComplete(true) }
                .addOnFailureListener { onComplete(false) }
        } else {
            onComplete(false)
        }
    }

    // Delete an expense from Firestore
    fun deleteExpense(expenseId: Int) {
        getUserExpensesRef()?.document(expenseId.toString())?.delete()
    }

    // Sync all expenses from Firestore (download to Room)
    fun syncFromFirestore(onDataReceived: (List<ExpenseEntity>) -> Unit) {
        val ref = getUserExpensesRef()
        if (ref != null) {
            ref.get().addOnSuccessListener { result ->
                val syncedExpenses = result.mapNotNull { doc ->
                    val id = doc.id.toIntOrNull()
                    val amount = doc.getDouble("amount")
                    val category = doc.getString("category")
                    val note = doc.getString("note")
                    val timestamp = doc.getLong("timestamp")

                    if (id != null && amount != null && category != null && note != null && timestamp != null) {
                        ExpenseEntity(
                            id = id,
                            amount = amount,
                            category = category,
                            note = note,
                            timestamp = timestamp,
                            isSynced = true
                        )
                    } else null
                }
                onDataReceived(syncedExpenses)
            }
        }
    }
}

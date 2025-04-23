package uk.ac.tees.mad.s3470478.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Defines a table named 'expenses' for Room persistence
@Entity(tableName = "expenses")
data class ExpenseEntity(
    // Primary key for each expense, auto-incremented
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    // Amount spent in this expense entry
    val amount: Double,

    // Category assigned to the expense
    val category: String,

    //description for the expense
    val note: String,

    // Timestamp indicated
    val timestamp: Long = System.currentTimeMillis(),

    // Indicates if this expense has been synced to Firestore
    val isSynced: Boolean = false
)

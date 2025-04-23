package uk.ac.tees.mad.s3470478.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    // Inserts a single expense into the database.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    // Updates a specific expense
    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    // Updates a list of expenses
    @Update
    suspend fun updateExpenses(expenses: List<ExpenseEntity>)

    // Deletes a specific expense
    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    // Returns a flow of all expenses
    @Query("SELECT * FROM expenses ORDER BY timestamp DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    // Returns a flow representing the total amount of all expenses
    @Query("SELECT SUM(amount) FROM expenses")
    fun getTotalAmount(): Flow<Double>

    // Returns a flow of all expenses that haven't been synced to Firestore
    @Query("SELECT * FROM expenses WHERE isSynced = 0")
    fun getUnsyncedExpenses(): Flow<List<ExpenseEntity>>

    // Deletes all entries in the expenses table
    @Query("DELETE FROM expenses")
    suspend fun clearAllExpenses()
}

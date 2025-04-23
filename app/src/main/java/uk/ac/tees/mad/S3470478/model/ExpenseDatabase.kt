package uk.ac.tees.mad.s3470478.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Defines the Room database with a single entity: ExpenseEntity
@Database(entities = [ExpenseEntity::class], version = 2)
abstract class ExpenseDatabase : RoomDatabase() {

    // Abstract function to get access to the DAO
    abstract fun expenseDao(): ExpenseDao

    companion object {
        // Ensures the database instance is shared across threads
        @Volatile private var INSTANCE: ExpenseDatabase? = null

        // Returns a singleton instance of the database
        fun getDatabase(context: Context): ExpenseDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    "expense_database"
                )
                    // Destroys and recreates the database if schema changes are detected
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
        }
    }
}

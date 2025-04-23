package uk.ac.tees.mad.s3470478.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uk.ac.tees.mad.s3470478.model.*
import uk.ac.tees.mad.s3470478.utils.ExpenseFirestoreHelper

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = ExpenseDatabase.getDatabase(application).expenseDao()

    // Holds the current list of all expenses, updated in real-time using Flow
    val expenses: StateFlow<List<ExpenseEntity>> = dao.getAllExpenses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Calculates the sum of all expenses for the current month
    val totalThisMonth = expenses
        .map { list -> list.sumOf { it.amount } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0.0)

    init {
        // Initiates syncing processes when the ViewModel is created
        syncUnsyncedExpensesToFirestore()
        syncFromFirestore()
    }

    // Inserts a new expense into the database and marks it as not yet synced
    fun addExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            dao.insertExpense(expense.copy(isSynced = false))
        }
    }

    // Updates an existing expense and resets its sync status
    fun updateExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            dao.updateExpense(expense.copy(isSynced = false))
        }
    }

    // Deletes an expense from both local database and Firestore
    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            dao.deleteExpense(expense)
            ExpenseFirestoreHelper.deleteExpense(expense.id)
        }
    }

    // Clears all local expenses — useful during logout or account switch
    fun clearLocalExpenses() {
        viewModelScope.launch {
            dao.clearAllExpenses()
        }
    }

    // Fetches all synced expenses from Firestore and replaces local cache
    fun refreshFromCloud() {
        viewModelScope.launch {
            clearLocalExpenses()
            ExpenseFirestoreHelper.syncFromFirestore { expensesFromCloud ->
                viewModelScope.launch {
                    expensesFromCloud.forEach { cloudExpense ->
                        dao.insertExpense(cloudExpense.copy(isSynced = true))
                    }
                }
            }
        }
    }

    // Uploads any locally stored, unsynced expenses to Firestore
    private fun syncUnsyncedExpensesToFirestore() {
        viewModelScope.launch {
            dao.getUnsyncedExpenses().collect { unsyncedList ->
                unsyncedList.forEach { expense ->
                    ExpenseFirestoreHelper.uploadExpense(expense) { success ->
                        if (success) {
                            viewModelScope.launch {
                                dao.updateExpense(expense.copy(isSynced = true))
                            }
                        }
                    }
                }
            }
        }
    }

    // Pulls data from Firestore and updates local cache, typically on app start
    private fun syncFromFirestore() {
        ExpenseFirestoreHelper.syncFromFirestore { expensesFromCloud ->
            viewModelScope.launch {
                expensesFromCloud.forEach { cloudExpense ->
                    dao.insertExpense(cloudExpense.copy(isSynced = true))
                }
            }
        }
    }
}

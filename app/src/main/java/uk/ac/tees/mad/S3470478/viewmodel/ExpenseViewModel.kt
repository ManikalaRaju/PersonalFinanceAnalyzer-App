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

    val expenses: StateFlow<List<ExpenseEntity>> = dao.getAllExpenses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalThisMonth = expenses
        .map { list -> list.sumOf { it.amount } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0.0)

    init {
        syncUnsyncedExpensesToFirestore()
        syncFromFirestore()
    }

    fun addExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            dao.insertExpense(expense.copy(isSynced = false))
        }
    }

    fun updateExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            dao.updateExpense(expense.copy(isSynced = false))
        }
    }

    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            dao.deleteExpense(expense)
            ExpenseFirestoreHelper.deleteExpense(expense.id)
        }
    }

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

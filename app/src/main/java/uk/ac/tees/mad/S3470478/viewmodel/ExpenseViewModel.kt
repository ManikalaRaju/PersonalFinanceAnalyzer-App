package uk.ac.tees.mad.S3470478.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uk.ac.tees.mad.S3470478.model.*

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = ExpenseDatabase.getDatabase(application).expenseDao()

    val expenses: StateFlow<List<ExpenseEntity>> = dao.getAllExpenses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalThisMonth = expenses
        .map { list -> list.sumOf { it.amount ?: 0.0 } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), 0.0)
    fun addExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            dao.insertExpense(expense)
        }
    }

    fun updateExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            dao.updateExpense(expense)
        }
    }

    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            dao.deleteExpense(expense)
        }
    }
}

package uk.ac.tees.mad.S3470478.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import uk.ac.tees.mad.S3470478.model.Expense

class ExpenseViewModel : ViewModel() {
    private val _expenses = mutableStateListOf<Expense>()
    val expenses: List<Expense> = _expenses

    fun addExpense(expense: Expense) {
        _expenses.add(expense)
    }
}

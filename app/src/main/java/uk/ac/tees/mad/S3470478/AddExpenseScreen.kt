package uk.ac.tees.mad.S3470478

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.S3470478.model.Expense
import uk.ac.tees.mad.S3470478.viewmodel.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(navController: NavHostController, viewModel: ExpenseViewModel) {
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Add Expense") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount") })
            OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category") })
            OutlinedTextField(value = note, onValueChange = { note = it }, label = { Text("Note") })

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                viewModel.addExpense(Expense(amount.toDoubleOrNull() ?: 0.0, category, note))
                navController.popBackStack()
            }) {
                Text("Save")
            }
        }
    }
}

package uk.ac.tees.mad.S3470478

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.S3470478.model.ExpenseEntity
import uk.ac.tees.mad.S3470478.viewmodel.ExpenseViewModel
import uk.ac.tees.mad.S3470478.utils.getCategoryIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(navController: NavHostController, viewModel: ExpenseViewModel) {
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    val categoryOptions = listOf(
        "Food", "Everyday Needs", "Entertainment", "Travel", "Health Care", "Shopping", "Rent", "Others"
    )
    var selectedCategory by remember { mutableStateOf(categoryOptions[0]) }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add Expense") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount (£)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = "${getCategoryIcon(selectedCategory)} $selectedCategory",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categoryOptions.forEach { category ->
                        DropdownMenuItem(
                            text = { Text("${getCategoryIcon(category)} $category") },
                            onClick = {
                                selectedCategory = category
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Note") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    val expense = ExpenseEntity(
                        amount = amount.toDoubleOrNull() ?: 0.0,
                        category = selectedCategory,
                        note = note
                    )
                    viewModel.addExpense(expense)
                    navController.popBackStack()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Save")
            }
        }
    }
}

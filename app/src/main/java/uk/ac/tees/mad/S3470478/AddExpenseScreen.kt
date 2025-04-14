package uk.ac.tees.mad.s3470478

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.s3470478.model.ExpenseEntity
import uk.ac.tees.mad.s3470478.viewmodel.ExpenseViewModel
import uk.ac.tees.mad.s3470478.utils.getCategoryIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    navController: NavHostController,
    viewModel: ExpenseViewModel,
    amountArg: String?,
    categoryArg: String?,
    noteArg: String?
) {
    val categoryOptions = listOf(
        "Food", "Everyday Needs", "Entertainment", "Travel", "Health Care", "Shopping", "Rent", "Others"
    )

    var amount by remember { mutableStateOf(amountArg ?: "") }
    var note by remember { mutableStateOf(noteArg ?: "") }
    var selectedCategory by remember {
        mutableStateOf(
            if (categoryArg != null && categoryOptions.contains(categoryArg)) categoryArg
            else categoryOptions[0]
        )
    }
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("➕ Add Expense") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Enter details to save a new expense.",
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount (£)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = "${getCategoryIcon(selectedCategory)} $selectedCategory",
                    onValueChange = {},
                    label = { Text("Category") },
                    trailingIcon = {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                    },
                    shape = RoundedCornerShape(12.dp),
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
                shape = RoundedCornerShape(12.dp),
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
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("💾 Save")
            }
        }
    }
}

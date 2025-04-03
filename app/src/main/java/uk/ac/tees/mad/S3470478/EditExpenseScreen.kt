package uk.ac.tees.mad.S3470478

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import uk.ac.tees.mad.S3470478.model.ExpenseEntity
import uk.ac.tees.mad.S3470478.viewmodel.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditExpenseScreen(
    navController: NavHostController,
    viewModel: ExpenseViewModel,
    expenseId: Int
) {
    val expenses by viewModel.expenses.collectAsState()
    val expense = expenses.find { it.id == expenseId }

    if (expense == null) {
        LaunchedEffect(Unit) { navController.popBackStack() }
        return
    }

    var amount by remember { mutableStateOf(expense.amount.toString()) }
    var note by remember { mutableStateOf(expense.note) }
    var selectedCategory by remember { mutableStateOf(expense.category) }
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val categoryOptions = listOf(
        "🍔 Food", "🧻 Everyday Needs", "🎮 Entertainment", "✈️ Travel",
        "💊 Health Care", "🛍 Shopping", "🏠 Rent", "📦 Others"
    )

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Edit Expense") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Edit Your Expense",
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount (£)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categoryOptions.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
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
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Delete", color = Color.White)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        val updated = expense.copy(
                            amount = amount.toDoubleOrNull() ?: 0.0,
                            category = selectedCategory,
                            note = note
                        )
                        viewModel.updateExpense(updated)
                        scope.launch {
                            snackbarHostState.showSnackbar("Expense updated!")
                        }
                        navController.popBackStack()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Update")
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Confirm Delete") },
                    text = { Text("Are you sure you want to delete this expense?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.deleteExpense(expense)
                                showDialog = false
                                navController.popBackStack()
                                scope.launch {
                                    snackbarHostState.showSnackbar("Expense deleted!")
                                }
                            }
                        ) {
                            Text("Delete", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

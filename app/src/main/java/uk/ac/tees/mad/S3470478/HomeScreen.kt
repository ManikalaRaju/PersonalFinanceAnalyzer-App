package uk.ac.tees.mad.s3470478

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import uk.ac.tees.mad.s3470478.model.ExpenseEntity
import uk.ac.tees.mad.s3470478.utils.getCategoryIcon
import uk.ac.tees.mad.s3470478.viewmodel.AuthenticationViewModel
import uk.ac.tees.mad.s3470478.viewmodel.ExpenseViewModel
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: ExpenseViewModel,
    authViewModel: AuthenticationViewModel,
    darkModeEnabled: Boolean,
    onToggleDarkMode: () -> Unit
) {
    val expenses by viewModel.expenses.collectAsState()
    val total by viewModel.totalThisMonth.collectAsState()
    val email = authViewModel.getCurrentUserEmail() ?: "Not signed in"
    val df = DecimalFormat("#,##0.00")
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var expenseToDelete by remember { mutableStateOf<ExpenseEntity?>(null) }
    var darkModeEnabled by remember { mutableStateOf(false) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(280.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
            ) {
                Text("👤 Logged in as", style = MaterialTheme.typography.labelMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(email, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(24.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🌙 Dark Mode", modifier = Modifier.weight(1f))
                    Switch(
                        checked = darkModeEnabled,
                        onCheckedChange = { onToggleDarkMode() }
                    )

                }

                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        authViewModel.logout()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Logout")
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("💼 Personal Finance Analyzer") },
                    actions = {
                        IconButton(onClick = {
                            coroutineScope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
            ) {
                // Top section: Total This Month
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        )
                        .padding(24.dp)
                ) {
                    Text(
                        text = "Total This Month: £${df.format(total)}",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom section: Recent Expenses
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Recent Expenses", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (expenses.isEmpty()) {
                        Text("No expenses yet.")
                    } else {
                        expenses.sortedByDescending { it.timestamp }.forEach { expense ->
                            ExpenseCard(
                                expense = expense,
                                onEditClick = { navController.navigate("edit/${expense.id}") },
                                onDeleteClick = { expenseToDelete = expense }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            // Deletion confirmation
            expenseToDelete?.let {
                AlertDialog(
                    onDismissRequest = { expenseToDelete = null },
                    title = { Text("Delete Expense") },
                    text = { Text("Are you sure you want to delete this expense?") },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.deleteExpense(it)
                            expenseToDelete = null
                        }) { Text("Delete") }
                    },
                    dismissButton = {
                        TextButton(onClick = { expenseToDelete = null }) { Text("Cancel") }
                    }
                )
            }
        }
    }
}

@Composable
fun ExpenseCard(
    expense: ExpenseEntity,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val df = DecimalFormat("#,##0.00")
    val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(expense.timestamp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = getCategoryIcon(expense.category),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(end = 12.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(expense.category, style = MaterialTheme.typography.titleMedium)
                    Text(
                        "£${df.format(expense.amount)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (expense.note.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = expense.note,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}

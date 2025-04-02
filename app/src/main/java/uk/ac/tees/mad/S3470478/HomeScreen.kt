package uk.ac.tees.mad.S3470478
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.S3470478.viewmodel.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, viewModel: ExpenseViewModel) {
    val expenses = viewModel.expenses

    Scaffold(
        topBar = { TopAppBar(title = { Text("Dashboard") }) },
        bottomBar = { BottomBar(navController) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Total This Month: £${expenses.sumOf { it.amount }}")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Recent Expenses:")
            expenses.forEach {
                Text("- ${it.category}: £${it.amount} (${it.note})")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("add") }) {
                Text("Add Expense")
            }
        }
    }
}

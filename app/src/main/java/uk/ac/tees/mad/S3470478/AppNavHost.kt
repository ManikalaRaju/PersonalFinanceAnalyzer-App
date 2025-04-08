package uk.ac.tees.mad.s3470478

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.*
import uk.ac.tees.mad.s3470478.viewmodel.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: ExpenseViewModel,
    padding: PaddingValues
) {
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route ?: "home"

    val showBottomBar = currentRoute == "home"
    val showTopBar = !showBottomBar

    val screenTitle = when {
        currentRoute.startsWith("add") -> "Add Expense"
        currentRoute.startsWith("edit") -> "Edit Expense"
        currentRoute == "camera" -> "Scan Receipt"
        currentRoute == "reports" -> "Reports"
        else -> ""
    }

    Scaffold(
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = { Text(screenTitle) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(navController, viewModel)
            }
            composable("camera") {
                CameraScreen(navController)
            }
            composable("reports") {
                ReportsScreen(viewModel)
            }
            composable("add") {
                AddExpenseScreen(
                    navController = navController,
                    viewModel = viewModel,
                    amountArg = null,
                    categoryArg = null,
                    noteArg = null
                )
            }
            composable(
                route = "add?amount={amount}&category={category}&note={note}",
                arguments = listOf(
                    navArgument("amount") { defaultValue = ""; nullable = true },
                    navArgument("category") { defaultValue = ""; nullable = true },
                    navArgument("note") { defaultValue = ""; nullable = true }
                )
            ) { backStackEntry ->
                AddExpenseScreen(
                    navController = navController,
                    viewModel = viewModel,
                    amountArg = backStackEntry.arguments?.getString("amount"),
                    categoryArg = backStackEntry.arguments?.getString("category"),
                    noteArg = backStackEntry.arguments?.getString("note")
                )
            }
            composable(
                route = "edit/{expenseId}",
                arguments = listOf(navArgument("expenseId") {
                    type = NavType.IntType
                })
            ) { backStackEntry ->
                val expenseId = backStackEntry.arguments?.getInt("expenseId") ?: -1
                EditExpenseScreen(
                    navController = navController,
                    viewModel = viewModel,
                    expenseId = expenseId
                )
            }
        }
    }
}

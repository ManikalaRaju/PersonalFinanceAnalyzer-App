package uk.ac.tees.mad.s3470478


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import uk.ac.tees.mad.s3470478.viewmodel.ExpenseViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    viewModel: ExpenseViewModel,
    padding: PaddingValues
) {
    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            HomeScreen(navController = navController, viewModel = viewModel)
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
        composable("camera") {
            CameraScreen(
                navController = navController,
                onImageCaptured = { uri ->
                    println("Captured URI: $uri")
                }
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

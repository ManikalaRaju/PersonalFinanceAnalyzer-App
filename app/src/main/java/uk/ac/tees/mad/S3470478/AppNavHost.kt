package uk.ac.tees.mad.S3470478

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.S3470478.model.Expense
import uk.ac.tees.mad.S3470478.viewmodel.ExpenseViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val viewModel = remember { ExpenseViewModel() }

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("home") { HomeScreen(navController, viewModel) }
        composable("add") { AddExpenseScreen(navController, viewModel) }
    }
}

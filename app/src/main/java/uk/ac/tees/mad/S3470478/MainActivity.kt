package uk.ac.tees.mad.s3470478

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.s3470478.ui.theme.PersonalFinanceAnalyzerTheme
import uk.ac.tees.mad.s3470478.viewmodel.ExpenseViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PersonalFinanceAnalyzerTheme {
                val navController = rememberNavController()
                val viewModel: ExpenseViewModel = viewModel()

                val currentRoute by remember {
                    derivedStateOf {
                        navController.currentBackStackEntry?.destination?.route
                    }
                }

                Scaffold(
                    bottomBar = {
                        if (currentRoute == "home") {
                            BottomNavigationBar(navController)
                        }
                    }
                ) { padding ->
                    AppNavHost(navController = navController, viewModel = viewModel, padding = padding)
                }
            }
        }
    }
}

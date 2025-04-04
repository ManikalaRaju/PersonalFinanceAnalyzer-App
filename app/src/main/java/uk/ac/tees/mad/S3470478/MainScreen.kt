package uk.ac.tees.mad.S3470478

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.S3470478.viewmodel.ExpenseViewModel

@Composable
fun MainScreen(viewModel: ExpenseViewModel) {
    val navController = rememberNavController()
    val items = listOf("home", "add", "camera")

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { screen ->
                    NavigationBarItem(
                        selected = false,
                        onClick = { navController.navigate(screen) },
                        icon = {
                            Icon(
                                imageVector = when (screen) {
                                    "home" -> Icons.Default.Home
                                    "add" -> Icons.Default.Add
                                    "camera" -> Icons.Default.Camera
                                    else -> Icons.Default.Home
                                },
                                contentDescription = screen
                            )
                        },
                        label = { Text(screen.replaceFirstChar { it.uppercase() }) }
                    )
                }
            }
        }
    ) { padding ->
        AppNavHost(
            navController = navController,
            viewModel = viewModel,
            padding = padding
        )
    }
}

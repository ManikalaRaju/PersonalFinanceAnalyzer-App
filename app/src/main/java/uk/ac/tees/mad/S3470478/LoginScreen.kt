package uk.ac.tees.mad.s3470478

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.s3470478.viewmodel.AuthenticationViewModel
import uk.ac.tees.mad.s3470478.viewmodel.AuthUiState
import uk.ac.tees.mad.s3470478.viewmodel.ExpenseViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: AuthenticationViewModel,
    expenseViewModel: ExpenseViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    // Automatically navigate to home after successful login
    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            Log.d("LoginScreen", "Login success. Navigating to home.")
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
            viewModel.resetState()
        }
    }

    // UI layout for login screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Triggers Firebase login and syncs expenses from Firestore
        Button(
            onClick = {
                viewModel.login(email.trim(), password.trim()) {
                    expenseViewModel.refreshFromCloud()
                }
            },
            enabled = uiState !is AuthUiState.Loading
        ) {
            Text("Login")
        }

        TextButton(onClick = { navController.navigate("signup") }) {
            Text("Don't have an account? Sign Up")
        }

        // Loading state indicator
        if (uiState is AuthUiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        }

        // Error message display
        if (uiState is AuthUiState.Error) {
            Text(
                text = (uiState as AuthUiState.Error).message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

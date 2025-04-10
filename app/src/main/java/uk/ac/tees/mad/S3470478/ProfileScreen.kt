package uk.ac.tees.mad.s3470478

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import uk.ac.tees.mad.s3470478.viewmodel.AuthenticationViewModel

@Composable
fun ProfileScreen(
    navController: NavHostController,
    viewModel: AuthenticationViewModel
) {
    val user = FirebaseAuth.getInstance().currentUser
    val email = user?.email ?: "No email"
    val name = user?.displayName ?: "Anonymous"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "👤 Profile", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Name: $name", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Email: $email", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            viewModel.logout()
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }) {
            Text("Logout")
        }
    }
}

package uk.ac.tees.mad.s3470478

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import uk.ac.tees.mad.s3470478.viewmodel.AuthenticationViewModel

@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: AuthenticationViewModel
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState(initial = false)

    // Automatically redirect after 2 seconds based on login status
    LaunchedEffect(Unit) {
        delay(2000)
        if (isLoggedIn) {
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    // Splash screen UI content
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B2A41)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Personal Finance Analyzer",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "\"Track smart. Spend wise.\"",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

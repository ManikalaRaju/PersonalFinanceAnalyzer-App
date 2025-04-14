package uk.ac.tees.mad.s3470478

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp // ✅ Add this import
import uk.ac.tees.mad.s3470478.AppNavHost
import uk.ac.tees.mad.s3470478.ui.theme.PersonalFinanceAnalyzerTheme
import uk.ac.tees.mad.s3470478.viewmodel.AuthenticationViewModel
import uk.ac.tees.mad.s3470478.viewmodel.ExpenseViewModel

class MainActivity : ComponentActivity() {

    private val expenseViewModel: ExpenseViewModel by viewModels()
    private val authViewModel: AuthenticationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        setContent {
            PersonalFinanceAnalyzerTheme {
                Surface {
                    val navController = rememberNavController()
                    AppNavHost(
                        navController = navController,
                        viewModel = expenseViewModel,
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}

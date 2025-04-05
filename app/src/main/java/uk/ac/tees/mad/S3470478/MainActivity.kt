package uk.ac.tees.mad.s3470478

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import uk.ac.tees.mad.s3470478.ui.theme.PersonalFinanceAnalyzerTheme
import uk.ac.tees.mad.s3470478.viewmodel.ExpenseViewModel
import uk.ac.tees.mad.s3470478.viewmodel.ExpenseViewModelFactory

class MainActivity : ComponentActivity() {
    private val expenseViewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PersonalFinanceAnalyzerTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    MainScreen(viewModel = expenseViewModel)
                }
            }
        }
    }
}

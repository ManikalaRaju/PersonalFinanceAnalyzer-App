package uk.ac.tees.mad.S3470478

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import uk.ac.tees.mad.S3470478.ui.theme.PersonalFinanceAnalyzerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PersonalFinanceAnalyzerTheme {
                AppNavHost()
            }
        }
    }
}

package uk.ac.tees.mad.s3470478

import android.R.attr.data
import android.R.attr.description
import android.graphics.Color as GColor
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.mikephil.charting.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import org.w3c.dom.Text
import uk.ac.tees.mad.s3470478.viewmodel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReportsScreen(viewModel: ExpenseViewModel = viewModel()) {
    val expenses = viewModel.expenses.collectAsState().value

    val categoryTotals = expenses.groupBy { it.category }
        .mapValues { it.value.sumOf { expense -> expense.amount } }

    val dayTotals = expenses.groupBy {
        SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(it.timestamp))
    }.mapValues { it.value.sumOf { expense -> expense.amount } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Text("📊 Expense Breakdown", style = MaterialTheme.typography.headlineSmall)

        // Pie Chart
        Text("By Category", style = MaterialTheme.typography.titleMedium)
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            factory = { context ->
                PieChart(context).apply {
                    data = PieData(PieDataSet(
                        categoryTotals.map { PieEntry(it.value.toFloat(), it.key) },
                        "Category"
                    ).apply {
                        colors = ColorTemplate.MATERIAL_COLORS.toList()
                        valueTextSize = 14f
                    })
                    description = Description().apply { text = "" }
                    animateY(1000)
                }
            }
        )

        // Bar Chart
        Text("By Day", style = MaterialTheme.typography.titleMedium)
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            factory = { context ->
                BarChart(context).apply {
                    data = BarData(BarDataSet(
                        dayTotals.entries.mapIndexed { index, entry ->
                            BarEntry(index.toFloat(), entry.value.toFloat())
                        },
                        "Daily Spending"
                    ).apply {
                        colors = ColorTemplate.COLORFUL_COLORS.toList()
                        valueTextSize = 14f
                    })
                    xAxis.valueFormatter = IndexAxisValueFormatter(dayTotals.keys.toList())
                    xAxis.granularity = 1f
                    xAxis.labelRotationAngle = -45f
                    description = Description().apply { text = "" }
                    animateY(1000)
                }
            }
        )
    }
}

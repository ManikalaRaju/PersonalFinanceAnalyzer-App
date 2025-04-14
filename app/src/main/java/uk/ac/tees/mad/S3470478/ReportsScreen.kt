package uk.ac.tees.mad.s3470478

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import uk.ac.tees.mad.s3470478.model.ExpenseEntity
import uk.ac.tees.mad.s3470478.viewmodel.ExpenseViewModel
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReportsScreen(viewModel: ExpenseViewModel = viewModel()) {
    val expenses = viewModel.expenses.collectAsState().value
    val tabTitles = listOf("📊 Charts", "📁 Past Expenses")
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            edgePadding = 8.dp
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> ChartsTab(expenses)
            1 -> PastExpensesTab(expenses)
        }
    }
}

@Composable
fun ChartsTab(expenses: List<ExpenseEntity>) {
    val categoryTotals = expenses.groupBy { it.category }
        .mapValues { it.value.sumOf { expense -> expense.amount } }

    val dayTotals = expenses.groupBy {
        SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(it.timestamp))
    }.mapValues { it.value.sumOf { expense -> expense.amount } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("📊 Expense Breakdown", style = MaterialTheme.typography.headlineSmall)

        Text("By Category", style = MaterialTheme.typography.titleMedium)
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            factory = { context ->
                PieChart(context).apply {
                    data = PieData(
                        PieDataSet(
                            categoryTotals.map { PieEntry(it.value.toFloat(), it.key) },
                            ""
                        ).apply {
                            colors = ColorTemplate.MATERIAL_COLORS.toList()
                            valueTextSize = 14f
                        }
                    )
                    description = Description().apply { text = "" }
                    animateY(1000)
                }
            }
        )

        Text("By Date", style = MaterialTheme.typography.titleMedium)
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            factory = { context ->
                BarChart(context).apply {
                    data = BarData(
                        BarDataSet(
                            dayTotals.entries.mapIndexed { index, entry ->
                                BarEntry(index.toFloat(), entry.value.toFloat())
                            },
                            ""
                        ).apply {
                            colors = ColorTemplate.COLORFUL_COLORS.toList()
                            valueTextSize = 14f
                        }
                    )
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

@Composable
fun PastExpensesTab(expenses: List<ExpenseEntity>) {
    val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    val groupedByMonth = expenses.groupBy {
        monthFormat.format(Date(it.timestamp))
    }.toSortedMap(compareByDescending { SimpleDateFormat("MMMM yyyy", Locale.getDefault()).parse(it) })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (groupedByMonth.isEmpty()) {
            Text("No past expenses available.", style = MaterialTheme.typography.bodyMedium)
        } else {
            groupedByMonth.forEach { (month, monthExpenses) ->
                Text(
                    text = month,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                monthExpenses.sortedByDescending { it.timestamp }.forEach { expense ->
                    PastExpenseItem(expense)
                    Divider(thickness = 0.5.dp, modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}

@Composable
fun PastExpenseItem(expense: ExpenseEntity) {
    val df = DecimalFormat("#,##0.00")
    val dateTime = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(expense.timestamp))

    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("£${df.format(expense.amount)}", style = MaterialTheme.typography.bodyLarge)
            Text(dateTime, style = MaterialTheme.typography.bodySmall)
        }
        if (expense.note.isNotBlank()) {
            Text(expense.note, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

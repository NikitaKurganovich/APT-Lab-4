package com.example.aptlab4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aptlab4.ui.theme.APTLab4Theme
import com.github.tehras.charts.*
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            APTLab4Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FinanceManager()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceManager() {
    var expense by remember { mutableStateOf(0) }
    var income by remember { mutableStateOf(0) }
    var totalExpense by remember { mutableStateOf(0) }
    var selectedSpendingType by remember { mutableStateOf(SpendingType.ENTERTAINMENT) }

    val pieChartData = remember {
        mutableStateListOf<PieChartData.Slice>()
    }
    val isNotEmptyChart by remember { derivedStateOf { pieChartData.isNotEmpty() } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = expense.toString(),
                onValueChange = { expense = it.toInt() },
                label = { Text("Expense") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 16.dp)
            )

            Box(modifier = Modifier.fillMaxWidth(0.4f)) {
                Text(
                    text = "Spending Type: ${selectedSpendingType.name}",
                    modifier = Modifier
                        .padding(end = 16.dp)
                )

                var expanded by remember { mutableStateOf(false) }
                IconButton(
                    onClick = { expanded = true },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SpendingType.values().forEach { type ->
                        DropdownMenuItem(
                            text = {
                                Text(type.name)
                            },
                            onClick = {
                                selectedSpendingType = type
                                expanded = false
                            })
                    }

                }
            }
        }

        Button(
            onClick = {
                totalExpense += expense
                val existingSliceIndex = pieChartData.toList().indexOfFirst { it.color == spendingMap[selectedSpendingType] }
                if (existingSliceIndex != -1) {
                    val existingSlice = pieChartData.toList()[existingSliceIndex]
                    val updatedSlice = existingSlice.copy(value = existingSlice.value + expense.toFloat())
                    pieChartData.toMutableList()[existingSliceIndex] = updatedSlice
                } else {
                    val newSlice = spendingMap[selectedSpendingType]?.let {
                        PieChartData.Slice(expense.toFloat(), it)
                    }
                    if (newSlice != null) {
                        pieChartData.add(newSlice)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add expense")
        }
        Text(
            text = "Your total spendings: $totalExpense",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        )
        Box(
            modifier = Modifier.fillMaxWidth(0.8f),
            contentAlignment = Alignment.Center
        ) {

                PieChart(
                    pieChartData = PieChartData(slices = pieChartData.toList()),
                    modifier = Modifier.height(200.dp)
                )

        }
    }
}


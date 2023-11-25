package com.example.aptlab4


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData


const val ENTER_EXPENSE_LABEL = "Enter your expenses"
const val ADD_EXPENSE_BUTTON_TEXT = "Add expense"
const val SPENDING_TYPE_LABEL = "Spending Type: "
const val YOUR_TOTAL_SPENDINGS_LABEL = "Your total spendings: "
const val NO_DATA_TO_DISPLAY = "No data to display"

@Composable
fun FinanceManager() {
    val expense = remember { mutableStateOf("") }
    val totalExpense = remember { mutableFloatStateOf(0.0F) }
    val selectedSpendingType = remember { mutableStateOf(SpendingType.ENTERTAINMENT) }

    val pieChartData = remember {
        mutableStateListOf<PieChartData.Slice>()
    }
    val isNotEmptyChart by remember { derivedStateOf { pieChartData.isNotEmpty() } }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExpenseInput(
            expense = expense,
            onValueChange = { expense.value = it.replace(",",".") },
            selectedSpendingType = selectedSpendingType
        )

        AddExpenseButton {
            totalExpense.floatValue += expense.value.toFloat()
            updatePieChartData(pieChartData, selectedSpendingType, expense)
        }

        TotalSpendings(totalExpense.floatValue)

        PieChartSection(pieChartData, isNotEmptyChart)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpenseInput(
    expense: MutableState<String>,
    onValueChange: (String) -> Unit,
    selectedSpendingType: MutableState<SpendingType>
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            value = expense.value,
            onValueChange = onValueChange,
            label = { Text(ENTER_EXPENSE_LABEL) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 16.dp)
        )

        Box(modifier = Modifier.fillMaxWidth(0.4f)) {
            Text(
                text = "$SPENDING_TYPE_LABEL${selectedSpendingType.value.name}",
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
                            selectedSpendingType.value = type
                            expanded = false
                        })
                }

            }
        }
    }
}



@Composable
private fun AddExpenseButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(ADD_EXPENSE_BUTTON_TEXT)
    }
}



@Composable
private fun TotalSpendings(totalExpense: Float) {
    Text(
        text = "$YOUR_TOTAL_SPENDINGS_LABEL$totalExpense",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
    )
}


@Composable
private fun PieChartSection(
    pieChartData: List<PieChartData.Slice>,
    isNotEmptyChart: Boolean
) {
    Box(
        modifier = Modifier.fillMaxWidth(0.8f),
        contentAlignment = Alignment.Center
    ) {
        if (isNotEmptyChart) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                PieChart(
                    pieChartData = PieChartData(slices = pieChartData.toList()),
                    modifier = Modifier.height(200.dp)
                )
                LazyColumn {
                    item {
                        Spacer(Modifier.height(20.dp))
                    }
                    itemsIndexed(pieChartData) { _, slice ->
                        Row{
                            val expenseType =
                                spendingMap.entries.find { it.value == slice.color }!!.key
                            Box(
                                modifier = Modifier
                                    .background(slice.color, RoundedCornerShape(100.dp))
                                    .height(20.dp)
                                    .width(20.dp)
                            )
                            Text(expenseType.name, Modifier.fillMaxWidth(0.6f))
                            Text(text = " ${slice.value} $")
                        }
                    }
                }
            }
        } else {
            Text(NO_DATA_TO_DISPLAY)
        }
    }
}

fun updatePieChartData(
    pieChartData: SnapshotStateList<PieChartData.Slice>,
    selectedSpendingType: MutableState<SpendingType>,
    expense: MutableState<String>
) {
    val existingSliceIndex = pieChartData.indexOfFirst { it.color == spendingMap[selectedSpendingType.value] }
    if (existingSliceIndex != -1) {
        val existingSlice = pieChartData[existingSliceIndex]
        val updatedSlice = existingSlice.copy(value = existingSlice.value + expense.value.toFloat())
        pieChartData[existingSliceIndex] = updatedSlice
    } else {
        val newSlice = spendingMap[selectedSpendingType.value]?.let {
            PieChartData.Slice(expense.value.toFloat(), it)
        }
        if (newSlice != null) {
            pieChartData.add(newSlice)
        }
    }
}
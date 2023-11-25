package com.example.aptlab4

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PersonalFinanceApp(viewModel: FinancialViewModel) {
    val spendings = viewModel.spendings

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        // Display spendings by month (You can use a RecyclerView or LazyColumn)

        // Button to add a new spending
        Button(onClick = { /* Open dialog here */ }) {
            Text("Add Spending")
        }
    }
}

@Composable
fun AddSpendingDialog(
    onSpendingAdded: (Spending) -> Unit,
    onDismiss: () -> Unit
) {
    // Implement your dialog here using AlertDialog or BottomSheetDialog
    // Include text fields for amount, a drop-down for spending type, and a date picker

    // Example:
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Spending") },
        confirmButton = {
            Button(onClick = {
                val spending = Spending(amount = 50f, type = SpendingType.ENTERTAINMENT, date = System.currentTimeMillis())
                onSpendingAdded(spending)
                onDismiss()
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

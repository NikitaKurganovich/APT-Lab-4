package com.example.aptlab4

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class FinancialViewModel : ViewModel() {
    private val _spendings = mutableStateListOf<Spending>()
    val spendings: List<Spending> get() = _spendings

    fun addSpending(spending: Spending) {
        _spendings.add(spending)
    }
}

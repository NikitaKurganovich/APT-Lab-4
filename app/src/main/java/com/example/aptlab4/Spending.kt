package com.example.aptlab4

import androidx.compose.ui.graphics.Color

data class Spending(val amount: Float, val type: SpendingType, val date: Long)

enum class SpendingType {
    ENTERTAINMENT,
    FOOD,
    REST,
    EDUCATION,
    TRANSPORT,
    OTHER
}

val spendingMap = mapOf(
    SpendingType.EDUCATION to Color.Green,
    SpendingType.FOOD to Color.Red,
    SpendingType.REST to Color.Yellow,
    SpendingType.EDUCATION to Color.Magenta,
    SpendingType.TRANSPORT to Color.Cyan,
    SpendingType.OTHER to Color.DarkGray
)
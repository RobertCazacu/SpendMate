package com.example.readwhritenotification

import java.util.Date

data class Transaction(
    val id: String = "",
    val name: String,
    val amount: Double,
    val date: Date = Date(),
    val category: String? = null
)
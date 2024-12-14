package com.hirumitha.budget.buddy.models

data class TransactionModel(
    val id: Long = 0,
    val type: String,
    val category: String,
    val amount: Float,
    val date: String
) {
    init {
        require(amount >= 0) { "Amount must be non-negative" }
    }
}
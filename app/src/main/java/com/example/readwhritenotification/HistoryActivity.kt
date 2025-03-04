package com.example.readwhritenotification

import Transaction
import TransactionAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransactionAdapter
    private var transactionList: MutableList<Transaction> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        transactionList = getTransactionHistory().toMutableList()
        adapter = TransactionAdapter(transactionList)
        recyclerView.adapter = adapter
    }

    private fun getTransactionHistory(): List<Transaction> {
        // TODO: Ia toate tranzacțiile din baza de date
        return listOf()
    }
}

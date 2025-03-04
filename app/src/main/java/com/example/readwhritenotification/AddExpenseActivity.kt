package com.example.readwhritenotification

import Transaction
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class AddExpenseActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var amountEditText: EditText
    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        nameEditText = findViewById(R.id.editTextName)
        amountEditText = findViewById(R.id.editTextAmount)
        addButton = findViewById(R.id.btnAdd)

        addButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val amount = amountEditText.text.toString().toDoubleOrNull()

            if (name.isNotEmpty() && amount != null) {
                val newTransaction = Transaction(
                    id = 0,
                    name = name,
                    amount = amount,
                    date = java.sql.Date(System.currentTimeMillis()), // Conversie corectă
                    description = "Tranzacție adăugată"
                )

                // TODO: Salvează tranzacția în baza de date
                finish() // Închide activitatea după adăugare
            } else {
                nameEditText.error = "Completează numele"
                amountEditText.error = "Introdu suma validă"
            }
        }
    }
}

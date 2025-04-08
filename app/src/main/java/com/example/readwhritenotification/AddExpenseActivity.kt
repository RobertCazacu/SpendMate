package com.example.readwhritenotification

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddExpenseActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var amountEditText: EditText
    private lateinit var addButton: Button
    private lateinit var progressBar: ProgressBar
    private val notionRepository = NotionRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        nameEditText = findViewById(R.id.editTextName)
        amountEditText = findViewById(R.id.editTextAmount)
        addButton = findViewById(R.id.btnAdd)
        progressBar = findViewById(R.id.progressBar) // Adaugă un ProgressBar în layout-ul tău

        addButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val amount = amountEditText.text.toString().toDoubleOrNull()

            if (name.isNotEmpty() && amount != null) {
                progressBar.visibility = View.VISIBLE
                addButton.isEnabled = false

                notionRepository.addTransaction(name, amount) { success, message ->
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        addButton.isEnabled = true

                        if (success) {
                            Toast.makeText(this, "Tranzacție adăugată cu succes", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, message ?: "Eroare la adăugarea tranzacției", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {
                if (name.isEmpty()) nameEditText.error = "Completează numele"
                if (amount == null) amountEditText.error = "Introdu suma validă"
            }
        }
    }
}
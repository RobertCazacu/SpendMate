package com.example.readwhritenotification

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InputDialogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_dialog)

        val title = intent.getStringExtra("transaction_title") ?: "Tranzacție"
        val text = intent.getStringExtra("transaction_text") ?: ""

        val titleView: TextView = findViewById(R.id.transactionTitle)
        val inputField: EditText = findViewById(R.id.inputReason)
        val confirmButton: Button = findViewById(R.id.btnConfirm)

        titleView.text = "Motiv pentru tranzacție: $title"

        confirmButton.setOnClickListener {
            val reason = inputField.text.toString()
            if (reason.isNotEmpty()) {
                val resultIntent = Intent()
                resultIntent.putExtra("transaction_reason", reason)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}
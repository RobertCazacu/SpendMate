package com.example.readwhritenotification

import android.os.Bundle
import android.view.View // Asigură-te că importi și View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class InputDialogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_dialog) // Asigură-te că layout-ul există

        val editText = findViewById<EditText>(R.id.editTextAmount)
        val button = findViewById<Button>(R.id.buttonSubmit)

        button.setOnClickListener {
            val amount = editText.text.toString()
            if (amount.isNotEmpty()) {
                Toast.makeText(this, "Suma introdusă: $amount", Toast.LENGTH_SHORT).show()
                finish() // Închide activitatea după completarea acțiunii
            } else {
                Toast.makeText(this, "Te rog să introduci o sumă", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

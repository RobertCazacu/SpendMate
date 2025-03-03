package com.example.readwhritenotification

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class InputDialogActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_dialog)

        val title = intent.getStringExtra("transaction_title") ?: "Tranzacție"
        val text = intent.getStringExtra("transaction_text") ?: ""
        val location = intent.getStringExtra("transaction_location") ?: ""

        val editTextAmount = findViewById<EditText>(R.id.editTextAmount)
        val editTextLocation = findViewById<EditText>(R.id.editTextLocation)
        val editTextReason = findViewById<EditText>(R.id.editTextReason)
        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)

        // Setează locația automată
        editTextLocation.setText(location)

        buttonSubmit.setOnClickListener {
            val amount = editTextAmount.text.toString()
            val location = editTextLocation.text.toString()
            val reason = editTextReason.text.toString()

            if (amount.isNotEmpty() && location.isNotEmpty() && reason.isNotEmpty()) {
                sendCustomNotification(amount, location, reason)
                Toast.makeText(this, "Informațiile au fost trimise", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Te rog să completezi toate câmpurile", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendCustomNotification(amount: String, location: String, reason: String) {
        val channelId = "response_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Răspuns tranzacție", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Răspuns Tranzacție")
            .setContentText("Suma: $amount | Locație: $location | Motiv: $reason")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(this).notify(1, notification)
    }
}
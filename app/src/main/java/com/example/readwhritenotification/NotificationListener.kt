package com.example.readwhritenotification

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn != null) {
            val packageName = sbn.packageName
            val extras = sbn.notification.extras
            val title = extras.getString("android.title") ?: "Tranzacție Necunoscută"
            val text = extras.getCharSequence("android.text")?.toString() ?: ""

            // Verificăm dacă notificarea provine de la aplicația dorită
            if (packageName == "com.example.testnotify") {
                sendCustomNotification(title, text)
            }
        }
    }

    private fun sendCustomNotification(title: String, text: String) {
        val channelId = "transaction_channel"

        // Creăm canalul de notificări pentru Android 8+ (Oreo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Tranzacții"
            val descriptionText = "Notificări pentru introducerea motivului tranzacției"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Creăm un PendingIntent care va deschide activitatea de introducere a motivului
        val intent = Intent(this, InputDialogActivity::class.java).apply {
            putExtra("transaction_title", title)
            putExtra("transaction_text", text)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Construim notificarea
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Confirmă tranzacția")
            .setContentText("Introdu motivul pentru: $title")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        // Trimitem notificarea
        with(NotificationManagerCompat.from(this)) {
            notify(1001, notification)
        }
    }
}
package com.example.readwhritenotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager

class NotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn != null) {
            val packageName = sbn.packageName
            val extras = sbn.notification.extras
            val title = extras.getString("android.title")
            val text = extras.getCharSequence("android.text")?.toString()

            // Verificăm dacă notificarea provine de la aplicația "TestNotify"
            if (packageName == "com.example.testnotify") {
                sendNotificationFromMyApp(title, text)
            }
        }
    }

    private fun sendNotificationFromMyApp(title: String?, text: String?) {
        val channelId = "my_app_channel"

        // Verificăm permisiunea de a trimite notificări
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permisiunea pentru notificări nu este acordată", Toast.LENGTH_SHORT).show()
            return
        }

        // Creăm canalul de notificări pentru Android 8+ (Oreo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My App Channel"
            val descriptionText = "Canal pentru notificările trimise de aplicația mea"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Creăm un PendingIntent pentru a deschide InputDialogActivity
        val intent = Intent(this, InputDialogActivity::class.java)
        intent.putExtra("title", title)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Construim notificarea
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Notificare de la aplicația mea")
            .setContentText("Adăugați suma cheltuită pentru: $title")
            .setContentIntent(pendingIntent) // Setăm PendingIntent pentru click
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // Trimitem notificarea
        with(NotificationManagerCompat.from(this)) {
            notify(1, notification)
        }

        // Afișăm un mesaj Toast pentru debugging
        Toast.makeText(this, "Notificare trimisă de aplicația mea", Toast.LENGTH_SHORT).show()
    }
}

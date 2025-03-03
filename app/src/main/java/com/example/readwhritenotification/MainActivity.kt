package com.example.readwhritenotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.readwhritenotification.ui.theme.ReadWhriteNotificationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verificăm dacă serviciul de notificări este activat
        if (!isNotificationListenerEnabled(this)) {
            // Trimitem utilizatorul la setările telefonului pentru a activa permisiunea
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            startActivity(intent)
        }

        // Cerem permisiunea pentru notificări (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
            }
        }

        setContent {
            ReadWhriteNotificationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Aplicație Notificări")

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(onClick = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
                                    if (ContextCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.POST_NOTIFICATIONS)
                                        != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(this@MainActivity,
                                            arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1001)
                                    } else {
                                        sendNotification()
                                    }
                                } else {
                                    sendNotification()
                                }
                            }) {
                                Text("Trimite Notificare")
                            }
                        }
                    }
                )
            }
        }
    }

    private fun sendNotification() {
        val channelId = "test_channel"

        // Creăm canalul de notificări pentru Android 8+ (Oreo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Test Channel"
            val descriptionText = "Canal pentru testarea notificărilor"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Construim notificarea
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("eMAG")
            .setContentText("Andrei si-a cumparat o papusa gonflabila")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // Trimitem notificarea
        with(NotificationManagerCompat.from(this)) {
            notify(1001, notification)
        }
    }

    // Funția de verificare a permisiunii de notificare
    fun isNotificationListenerEnabled(context: Context): Boolean {
        val packageName = context.packageName
        val enabledListeners = Settings.Secure.getString(
            context.contentResolver,
            "enabled_notification_listeners"
        )
        return enabledListeners?.let {
            it.split(":").any { componentName -> componentName.contains(packageName) }
        } ?: false
    }
}
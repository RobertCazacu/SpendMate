package com.example.readwhritenotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.readwhritenotification.ui.theme.ReadWhriteNotificationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verificăm dacă permisiunea pentru notificări a fost deja acordată
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Cerem permisiunea pentru notificări (Android 13+)
            val requestPermissionLauncher =
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                    if (!isGranted) {
                        Toast.makeText(this, "Permisiunea pentru notificări a fost refuzată", Toast.LENGTH_SHORT).show()
                    }
                }
            requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
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
                                if (ActivityCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                                    sendTestNotification(this@MainActivity)
                                } else {
                                    Toast.makeText(this@MainActivity, "Permisiunea pentru notificări nu este acordată", Toast.LENGTH_SHORT).show()
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

    // Funcție pentru trimiterea unei notificări de test
    private fun sendTestNotification(context: Context) {
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
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Construim notificarea
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("eMAG")
            .setContentText("Andrei și-a cumpărat o păpușă gonflabilă")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // Verificăm dacă avem permisiunea de notificare
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            // Avem permisiunea, putem trimite notificarea
            with(NotificationManagerCompat.from(context)) {
                notify(1, notification)
            }
        } else {
            // Nu avem permisiunea, informăm utilizatorul
            Toast.makeText(context, "Permisiunea pentru notificări nu este acordată", Toast.LENGTH_SHORT).show()
        }
    }
}

package com.example.readwhritenotification

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
import com.example.readwhritenotification.ui.theme.ReadWhriteNotificationTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val notificationHelper = NotificationHelper(this)

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (!isGranted) {
                    Toast.makeText(this, "Permisiunea pentru notificări a fost refuzată", Toast.LENGTH_SHORT).show()
                }
            }

        // Cerem permisiunea pentru notificări (Android 13+)
        requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)

        setContent {
            ReadWhriteNotificationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
                            notificationHelper.showNotification(
                                "Notificare de test",
                                "Aceasta este o notificare simplă."
                            )
                        }) {
                            Text("Trimite Notificare")
                        }
                    }
                }
            }
        }
    }
}

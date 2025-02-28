package com.example.readwhritenotification

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationListener : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn != null) {
            val packageName = sbn.packageName
            val extras = sbn.notification.extras
            val title = extras.getString("android.title")
            val text = extras.getCharSequence("android.text")?.toString()

            Log.d("NotificationListener", "Notificare de la: $packageName - $title: $text")

            // Verificăm dacă notificarea provine de la aplicațiile de interes
            if (packageName == "com.banca.app" || packageName == "com.revolut" ||
                packageName == "com.dex.app" || packageName == "com.bts.app") {
                // Aici poți trimite notificarea mai departe sau să o procesezi
                processNotification(title, text)
            }
        }
    }

    private fun processNotification(title: String?, text: String?) {
        Log.d("NotificationListener", "Procesare notificare: $title - $text")
        // Poți trimite notificarea către UI sau să o salvezi în baza de date
    }
}

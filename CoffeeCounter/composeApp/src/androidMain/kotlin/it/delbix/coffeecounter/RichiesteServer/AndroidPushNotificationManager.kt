package it.delbix.coffeecounter.RichiesteServer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessaging
import it.delbix.coffeecounter.R

class AndroidPushNotificationManager(private val context: Context) : PushNotificationManager {

    override fun getToken(): String {
        return FirebaseMessaging.getInstance().token.result!!
    }

    override fun onNotificationReceived(data: Map<String, String>) {

        val title = data["title"] ?: "Nuova notifica"
        val message = data["body"] ?: "Hai ricevuto un messaggio"

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crea il canale di notifica (necessario da Android 8.0 in poi)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default_channel_id",
                "Notifiche",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, "default_channel_id")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Assicurati di avere un'icona
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(0, notification)
    }

}





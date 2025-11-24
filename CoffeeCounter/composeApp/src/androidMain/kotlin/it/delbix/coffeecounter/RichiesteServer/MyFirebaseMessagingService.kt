package it.delbix.coffeecounter.RichiesteServer

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.util.Log
import com.google.firebase.messaging.Constants.TAG
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.content.Context
import androidx.core.app.NotificationCompat


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        // Invia il token al tuo backend
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        val pushManager = AndroidPushNotificationManager(applicationContext)

        // Gestione standard
        pushManager.onNotificationReceived(data)

        // Se l'app è in foreground, mostra una notifica (o aggiorna la UI)
        if (isAppInForeground()) {
            showNotification( data["title"] ?: remoteMessage.notification?.title ?: "Notifica", data["message"] ?: remoteMessage.notification?.body ?: "")
        }
    }

    fun isAppInForeground(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false

        val packageName = applicationContext.packageName
        for (appProcess in appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND &&
                appProcess.processName == packageName) {
                return true
            }
        }
        return false
    }


    private fun showNotification(title: String, message: String) {
        val channelId = "default_channel_id"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
            channelId,
            "Notifiche generiche",
            NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
//        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(android.R.drawable.btn_star) // Assicurati che esista
            .setAutoCancel(true)
            .build()

        notificationManager.notify(0, notification)
    }


}


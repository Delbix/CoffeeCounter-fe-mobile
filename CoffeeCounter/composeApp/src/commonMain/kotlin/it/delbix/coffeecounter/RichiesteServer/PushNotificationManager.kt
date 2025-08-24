package it.delbix.coffeecounter.RichiesteServer

    interface PushNotificationManager {
        fun getToken(): String
        fun onNotificationReceived(data: Map<String, String>)
    }

    expect fun getPushNotificationManager(): PushNotificationManager



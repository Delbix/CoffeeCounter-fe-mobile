package it.delbix.coffeecounter.RichiesteServer

actual fun getPushNotificationManager(): PushNotificationManager = object : PushNotificationManager {
    override fun getToken(): String = ""

    override fun onNotificationReceived(data: Map<String, String>) {
        //TODO magari un giorno da implementare
        // Stub: non fa nulla
    }
}



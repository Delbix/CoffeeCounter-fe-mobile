package it.delbix.coffeecounter.RichiesteServer

import android.content.Context

private lateinit var pushManager: PushNotificationManager

fun initPushNotificationManager(context: Context) {
    pushManager = AndroidPushNotificationManager(context)
}

actual fun getPushNotificationManager(): PushNotificationManager {
    return pushManager
}

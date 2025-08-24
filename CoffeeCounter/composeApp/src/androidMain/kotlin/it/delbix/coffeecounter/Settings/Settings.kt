package it.delbix.coffeecounter.Settings

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

lateinit var appContext: Context

fun initSettings(context: Context) {
    appContext = context.applicationContext
}

actual fun getSettings(): Settings {
    val sharedPreferences = appContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    return SharedPreferencesSettings(sharedPreferences)
}

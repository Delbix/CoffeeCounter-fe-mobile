package it.delbix.coffeecounter.Settings

import com.russhwolf.settings.Settings
import com.russhwolf.settings.PreferencesSettings
import java.util.prefs.Preferences

actual fun getSettings(): Settings {
    val preferences = Preferences.userRoot().node("MyAppSettings")
    return PreferencesSettings(preferences)
}

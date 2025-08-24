package it.delbix.coffeecounter.utils

actual object Logger {
    actual fun log(message: String) {
        android.util.Log.d("Logger", message)
    }
}

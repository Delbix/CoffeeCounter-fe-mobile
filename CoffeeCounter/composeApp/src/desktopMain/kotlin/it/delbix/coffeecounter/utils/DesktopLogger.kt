package it.delbix.coffeecounter.utils

actual object Logger {
    actual fun log(message: String) {
        println("Logger: $message")
    }
}

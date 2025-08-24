package it.delbix.coffeecounter.utils

import platform.Foundation.NSLog

actual object Logger {
    actual fun log(message: String) {
        NSLog("Logger: %@", message)
    }
}

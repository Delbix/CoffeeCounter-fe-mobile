package it.insiel.coffeecounter.utils

import co.touchlab.kermit.Logger

val logger = Logger.withTag("CoffeeLog")

fun logMessage(message: String) {
    logger.d { message }
}
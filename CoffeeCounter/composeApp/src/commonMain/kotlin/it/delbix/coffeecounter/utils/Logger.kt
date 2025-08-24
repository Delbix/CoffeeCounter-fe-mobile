package it.delbix.coffeecounter.utils

//import co.touchlab.kermit.Logger
//
//val logger = Logger.withTag("CoffeeLog")
//
//fun logMessage(message: String) {
//    logger.d { message }
//}

expect object Logger {
    fun log(message: String)
}

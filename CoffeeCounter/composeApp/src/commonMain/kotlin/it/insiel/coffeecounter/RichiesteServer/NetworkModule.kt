package it.insiel.coffeecounter.RichiesteServer

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.plugins.logging.*

/**
 * MODULO
 * che gestisce la connessione al serve di backend
 */

expect fun getHttpClientEngine(): HttpClientEngine

fun createHttpClient(): HttpClient {
    return HttpClient(getHttpClientEngine()) {
        install(ContentNegotiation) {
            json()
        }
        install(Logging) {
            level = LogLevel.BODY
        }
    }
}
package it.insiel.coffeecounter.RichiesteServer

import io.ktor.client.request.*
import io.ktor.client.statement.*
import it.insiel.coffeecounter.AppInit
import kotlinx.serialization.json.Json

/**
 * MODULO
 * per la gestione delle richieste di dati al server
 */

/**
 * CLASSE
 * che determina il tipo di ritorno per le funzioni presenti in questo modulo
 */
sealed class Result {
    data class Success(val data: List<Persona>) : Result()
    data class Error(val message: String) : Result()
}

/**
 * Chiama il server per chiedere l'elenco di persone salvate nel database
 */
suspend fun fetchPersonas(): Result {
    return try {
        val client = createHttpClient()
        val response: HttpResponse = client.get("${AppInit.API_URL}/angular/transazione")
        val responseBody: String = response.bodyAsText()
        val personas = Json.decodeFromString<List<Persona>>(responseBody)
        Result.Success(personas)
    } catch (e: Exception) {
        Result.Error("Errore nella connessione al server: ${e.message}")
    }
}
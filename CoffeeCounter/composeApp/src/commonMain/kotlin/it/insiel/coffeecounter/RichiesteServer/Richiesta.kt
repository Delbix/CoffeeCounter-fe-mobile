package it.insiel.coffeecounter.RichiesteServer

import io.ktor.client.request.*
import io.ktor.client.statement.*
import it.insiel.coffeecounter.AppInit
import kotlinx.serialization.json.Json

/**
 * MODULO
 * per la gestione delle richieste di dati al server
 */

object Richiesta: RichiestaDatiService {

    /**
     * Chiama il server per chiedere l'elenco di persone salvate nel database
     */
    override suspend fun fetchPersonas(): Result {
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
}
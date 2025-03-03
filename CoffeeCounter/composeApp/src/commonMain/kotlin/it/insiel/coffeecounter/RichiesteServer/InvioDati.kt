package it.insiel.coffeecounter.RichiesteServer

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import it.insiel.coffeecounter.AppInit
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * MODULO
 * che gestisce l'invio di dati al serve sotto forma di chiamate http
 */

object InvioDati: InvioDatiService {

    override val client = createHttpClient()


    /**
     * Inserimento di una transazione in db
     * @param transazione [Transazione] = transazione da inserire
     * @return HttpResponse, con il contenuto della risposta del server
     */
    override suspend fun sendTransazione(transazione: Transazione): Transazione {
        //val client = createHttpClient()
        val response = client.post("${AppInit.API_URL}/angular/db/transazione") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(transazione))
        }
        return Json.decodeFromString<Transazione>(response.bodyAsText())
    }

    /**
     * Inserimento/modifica di un utente in db
     * @param persona [Persona] = utente da inserire/modificare
     * @return HttpResponse, con il contenuto della risposta del server
     */
    override suspend fun sendPersona(persona: Persona): Persona {
        //val client = createHttpClient()
        val response = client.post("${AppInit.API_URL}/angular/db/persona") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(persona))
        }
        return Json.decodeFromString<Persona>(response.bodyAsText())
    }

    //per l'eliminazione di un utente
    /**
     * Eliminazione di un utente dal db
     * @param persona [Persona] = utente da eliminare
     * @return HttpResponse, con il contenuto della risposta del server
     */
    override suspend fun eliminaPersona(persona: Persona): String {
        //val client = createHttpClient()
        val response = client.post("${AppInit.API_URL}/angular/db/persona/elimina") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(persona))
        }
        return response.bodyAsText()
    }
}
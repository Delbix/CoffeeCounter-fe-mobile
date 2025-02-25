package it.insiel.coffeecounter.RichiesteServer

import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import it.insiel.coffeecounter.AppInit
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * MODULO
 * che gestisce l'invio di dati al serve sotto forma di chiamate http
 */


/**
 * Inserimento di una transazione in db
 * @param transazione [Transazione] = transazione da inserire
 * @return HttpResponse, con il contenuto della risposta del server
 */
suspend fun sendTransazione(transazione: Transazione): HttpResponse {
    val client = createHttpClient()
    return client.post("${AppInit.API_URL}/angular/db/transazione") {
        contentType(ContentType.Application.Json)
        setBody(Json.encodeToString(transazione))
    }
}

/**
 * Inserimento/modifica di un utente in db
 * @param persona [Persona] = utente da inserire/modificare
 * @return HttpResponse, con il contenuto della risposta del server
 */
suspend fun sendPersona( persona: Persona ): HttpResponse{
    val client = createHttpClient()
    return client.post("${AppInit.API_URL}/angular/db/persona") {
        contentType(ContentType.Application.Json)
        setBody(Json.encodeToString(persona))
    }
}

//per l'eliminazione di un utente
/**
 * Eliminazione di un utente dal db
 * @param persona [Persona] = utente da eliminare
 * @return HttpResponse, con il contenuto della risposta del server
 */
suspend fun eliminaPersona( persona: Persona ): HttpResponse{
    val client = createHttpClient()
    return client.post("${AppInit.API_URL}/angular/db/persona/elimina") {
        contentType(ContentType.Application.Json)
        setBody(Json.encodeToString(persona))
    }
}
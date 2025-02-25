package it.insiel.coffeecounter.RichiesteServer

import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import it.insiel.coffeecounter.AppInit
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

//per l'inserimento di una transazione nel db
suspend fun sendTransazione(transazione: Transazione): HttpResponse {
    val client = createHttpClient()
    return client.post("${AppInit.API_URL}/angular/db/transazione") {
        contentType(ContentType.Application.Json)
        setBody(Json.encodeToString(transazione))
    }
}

//per l'inserimento di un nuovo utente in db
suspend fun sendPersona( persona: Persona ): HttpResponse{
    val client = createHttpClient()
    return client.post("${AppInit.API_URL}/angular/db/persona") {
        contentType(ContentType.Application.Json)
        setBody(Json.encodeToString(persona))
    }
}

//per l'eliminazione di un utente
suspend fun eliminaPersona( persona: Persona ): HttpResponse{
    val client = createHttpClient()
    return client.post("${AppInit.API_URL}/angular/db/persona/elimina") {
        contentType(ContentType.Application.Json)
        setBody(Json.encodeToString(persona))
    }
}
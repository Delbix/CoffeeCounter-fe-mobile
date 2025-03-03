package it.insiel.coffeecounter.RichiesteServer

import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponse

interface InvioDatiService {
    public val client: HttpClient

    suspend fun sendTransazione(transazione: Transazione): Transazione

    suspend fun sendPersona(persona: Persona): Persona

    suspend fun eliminaPersona(persona: Persona): String
}
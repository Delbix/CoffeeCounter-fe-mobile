package it.delbix.coffeecounter.RichiesteServer

import io.ktor.client.HttpClient

interface InvioDatiService {
    val client: HttpClient

    suspend fun sendTransazione(transazione: Transazione): Transazione

    suspend fun sendPersona(persona: Persona): Persona

    suspend fun eliminaPersona(persona: Persona): String

    suspend fun inviaNotifica( notifica: Notifica ): String
}
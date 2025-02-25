package it.insiel.coffeecounter.RichiesteServer

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import it.insiel.coffeecounter.AppInit
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

//suspend fun fetchPersonas(): List<Persona> {
//    val client = createHttpClient()
//    val response: HttpResponse = client.get("http://localhost:8080/angular/transazione")
//    val responseBody: String = response.bodyAsText()
//    return Json.decodeFromString(responseBody)
//}

sealed class Result {
    data class Success(val data: List<Persona>) : Result()
    data class Error(val message: String) : Result()
}

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
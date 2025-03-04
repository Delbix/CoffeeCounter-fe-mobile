package it.insiel.coffeecounter.RichiesteServer


interface RichiestaDatiService {

    suspend fun fetchPersonas(): Result
}
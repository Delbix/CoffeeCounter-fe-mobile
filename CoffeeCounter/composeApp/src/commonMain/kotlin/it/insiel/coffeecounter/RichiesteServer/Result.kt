package it.insiel.coffeecounter.RichiesteServer

/**
 * CLASSE
 * che determina il tipo di ritorno con un elemento in caso di successo ed uno in caso di errore
 */
sealed class Result {
    data class Success(val data: List<Persona>) : Result()
    data class Error(val message: String) : Result()
}
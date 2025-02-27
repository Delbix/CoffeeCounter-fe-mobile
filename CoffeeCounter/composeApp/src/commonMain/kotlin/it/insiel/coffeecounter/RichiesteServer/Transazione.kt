package it.insiel.coffeecounter.RichiesteServer

import kotlinx.serialization.Serializable

/**
 * DATO
 * Transazione, per mandare e ricevere DTO al server
 */

@Serializable
data class Transazione(
    val id              : Int? = null,
    val data            : String,
    val partecipanti    : MutableList<Persona> = mutableListOf(), //mutable list mi permette di fare add e remove
    val pagata_da       : Persona? //id di chi paga
)


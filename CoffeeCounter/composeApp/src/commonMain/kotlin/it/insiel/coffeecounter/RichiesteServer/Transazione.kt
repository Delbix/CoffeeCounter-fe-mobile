package it.insiel.coffeecounter.RichiesteServer

import kotlinx.serialization.Serializable

/**
 * DATO
 * Transazione, per mandare e ricevere DTO al server
 */

@Serializable
data class Transazione(
    var id              : Int? = null,
    var data            : String,
    var partecipanti    : MutableList<Persona> = mutableListOf(), //mutable list mi permette di fare add e remove
    var pagata_da       : Persona? //id di chi paga
)


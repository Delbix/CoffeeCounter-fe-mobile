package it.delbix.coffeecounter.RichiesteServer

import kotlinx.serialization.Serializable

/**
 * DATO
 * Notifica, per mandare e ricevere DTO al server
 */

@Serializable
data class Notifica(
    var titolo: String,
    var messaggio: String,
    var topic: String
)

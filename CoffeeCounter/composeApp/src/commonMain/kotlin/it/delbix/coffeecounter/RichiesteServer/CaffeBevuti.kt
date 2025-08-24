package it.delbix.coffeecounter.RichiesteServer

import kotlinx.serialization.Serializable

/**
 * DATO
 * CaffeBevuti, per ricezione delle statistiche del server
 */

@Serializable
data class CaffeBevuti(
    var day0        : Int,
    var Mese        : Int,
    var Settimana   : Int,
)
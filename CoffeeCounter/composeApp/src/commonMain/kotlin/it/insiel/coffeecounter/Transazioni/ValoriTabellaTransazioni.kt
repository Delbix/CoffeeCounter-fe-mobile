package it.insiel.coffeecounter.Transazioni

import it.insiel.coffeecounter.RichiesteServer.Persona
import it.insiel.coffeecounter.RichiesteServer.Transazione

/**
 * Struttura dati condivisa per la gestione della vista delle transazioni
 */

data class ValoriTabellaTransazioni(
    val persone : List<Persona>,
    val errorMessage: String? = null,
    val transazione: Transazione = Transazione(null, "", mutableListOf(), null),
    val rate: Double = 2.0, //il rate massimo =1, quindi impostandolo a 2 il primo spuntato viene sempre preso come pagatore
)

package it.delbix.coffeecounter.Transazioni

import it.delbix.coffeecounter.RichiesteServer.Persona
import it.delbix.coffeecounter.RichiesteServer.Transazione

/**
 * Struttura dati condivisa per la gestione della vista delle transazioni
 */

data class TransazioniUI(
    val persone : List<Persona> = emptyList(),
    val errorMessage: String? = null,
    val transazione: Transazione = Transazione(null, "", mutableListOf(), null),
    val rate: Double = 2.0, //il rate massimo =1, quindi impostandolo a 2 il primo spuntato viene sempre preso come pagatore
)

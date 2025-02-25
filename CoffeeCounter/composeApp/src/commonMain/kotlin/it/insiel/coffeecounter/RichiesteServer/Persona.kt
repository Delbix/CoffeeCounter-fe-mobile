package it.insiel.coffeecounter.RichiesteServer

import kotlinx.serialization.Serializable

/**
 * DATO
 * Persona, per mandare e ricevere DTO al server
 */

@Serializable
data class Persona(
    var id              : Int,
    var nome            : String,
    var cognome         : String,
    var ha_pagato       : Int,
    var ha_partecipato  : Int,
    var checked         : Boolean = false
)

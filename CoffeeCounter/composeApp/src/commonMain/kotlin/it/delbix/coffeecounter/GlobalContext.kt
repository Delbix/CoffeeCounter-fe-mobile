package it.delbix.coffeecounter

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import it.delbix.coffeecounter.RichiesteServer.Persona

class GlobalContext: ViewModel() {
    private val _currentPersonaDaModificare = mutableStateOf<Persona>(Persona(id = -2, nome = "", cognome = "", ha_pagato = 0, ha_partecipato = 0, caffe_pagati = 0))
    val currentPersonaDaModificare: State<Persona> = _currentPersonaDaModificare

    fun setPersonaDaModificare( persona: Persona ){
        _currentPersonaDaModificare.value = persona
    }
}
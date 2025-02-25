package it.insiel.coffeecounter.Transazioni

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readText
import io.ktor.util.InternalAPI
import it.insiel.coffeecounter.RichiesteServer.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


/**
 * VISTA della creazione di una transazione
 * @OLD [non utilizzata]
 */

//mi servono come variabili globali perchè le devo usare nelle righe
var transazione = Transazione(null, "", mutableListOf(), null)
var rate: Int = 2; //il rate massimo =1, quindi impostandolo a 2 il primo spuntato viene sempre preso come pagatore

@OptIn(InternalAPI::class)
@Composable
fun vistaTransazioni(){

    //variabili locali
    var persone by remember { mutableStateOf<List<Persona>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var responseMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        scope.launch {
            when (val result = fetchPersonas()) {
                is Result.Success -> {
                    persone = result.data
                    errorMessage = null
                }
                is Result.Error -> {
                    persone = emptyList()
                    errorMessage = result.message
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (errorMessage != null) {
            Text(text = errorMessage!!, color = Color.Red, modifier = Modifier.padding(8.dp))
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                        .background(Color.Blue)
                        .border(1.dp, Color.Black)
                ) {
                    Text(text = "ID", modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "Nome Cognome", modifier = Modifier.weight(2f),
                        fontWeight = FontWeight.Bold)
                    Text(text = "Partecipa", modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold)
                }
            }
            items(persone) { persona ->
                PersonaRow(persona)
            }
            item {
                Button(onClick = {
                    scope.launch {
                        try {
                            transazione.data = Clock.System.now().toLocalDateTime( TimeZone.currentSystemDefault() ).date.toString()
                            val response = sendTransazione(transazione)
                            responseMessage = "Dati inviati con successo: ${response.status}"
                            responseMessage = "Dati inviati con successo: ${response.bodyAsText()}"
                        } catch (e: Exception) {
                            responseMessage = "Errore nell'invio dei dati: ${e.message}"
                        }
                    }
                }) {
                    Text("Invia")
                }
            }
            item {
                if (responseMessage != null) {
                    Text(text = responseMessage!!, color = Color.Red, modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}

@Composable
fun PersonaRow(persona: Persona) {
    var partecipa by remember { mutableStateOf(persona.checked) }
    var ratePersona = 0
    if ( persona.ha_partecipato != 0 ){
        ratePersona = persona.ha_pagato / persona.ha_partecipato
    }

    var backgroundColor = if (partecipa && transazione.pagata_da == persona) Color.Green else Color.White

    Row(modifier = Modifier.fillMaxWidth().padding(8.dp)
        .border(1.dp, Color.Black).background(backgroundColor),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = persona.id.toString(), modifier = Modifier.weight(1f))
        Text(text = "${persona.nome} ${persona.cognome} - $ratePersona", modifier = Modifier.weight(2f))
        Checkbox(
            checked = partecipa,
            onCheckedChange = {
                //logica del cambio di stato
                partecipa = it
                persona.checked = it
                if (partecipa) {
                    transazione.partecipanti.add( persona )

                    if ( ratePersona < rate ) {
                        rate = ratePersona
                        transazione.pagata_da = persona
                        backgroundColor = Color.Green
                    }
                } else {
                    transazione.partecipanti.remove( persona )
                    if ( transazione.pagata_da == persona ){
                        //TODO gestire la rimozione dal pagamento
                    }
                }
            },
            modifier = Modifier.weight(1f)
        )
    }
}
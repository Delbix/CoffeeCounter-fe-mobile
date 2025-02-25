package it.insiel.coffeecounter.GestioneUtenti

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import it.insiel.coffeecounter.RichiesteServer.Persona

/**
 * gestisce una riga di tipo persona
 * @Require di essere chiamato da UserTable
 *
 * onClickVisualizza = gestione della vista di modifica
 */

@Composable
fun RigaUtente( persona: Persona, onClickVisualizza: (Persona) -> Unit ) {

    Row(modifier = Modifier.fillMaxWidth().padding(3.dp)
        .background(Color.LightGray, RoundedCornerShape(4.dp)),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = "${persona.nome} ${persona.cognome}", modifier = Modifier.padding(8.dp).weight(1f).align(Alignment.CenterVertically) )
        Text( text = "${persona.ha_pagato}/${persona.ha_partecipato}", modifier = Modifier.padding(8.dp).weight(1f).align(Alignment.CenterVertically) )
        Button(onClick = { onClickVisualizza( persona ) }, modifier = Modifier.padding(8.dp).weight(1f) ) {
            Text("Modifica")
        }
    }
}
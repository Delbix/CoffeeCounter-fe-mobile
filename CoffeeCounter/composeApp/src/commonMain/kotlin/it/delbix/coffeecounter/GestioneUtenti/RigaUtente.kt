package it.delbix.coffeecounter.GestioneUtenti

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coffeecounter.composeapp.generated.resources.Res
import coffeecounter.composeapp.generated.resources.modifica
import it.delbix.coffeecounter.GlobalContext
import it.delbix.coffeecounter.RichiesteServer.Persona
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * MODULO
 * gestisce una riga di tipo persona
 * @Require di essere chiamato da UserTable
 *
 * **Parametri**
 *  @param persona [Persona] = persona oggetto di questa riga
 * **Lambda**
 * @param onClickVisualizza = gestione della vista di modifica della persona in oggetto
 */

@Composable
fun RigaUtente( persona: Persona, onClickVisualizza: (Persona) -> Unit ) {
    //viewModel
    val globalContext: GlobalContext = koinViewModel<GlobalContext>()
    Row(modifier = Modifier.fillMaxWidth().padding(3.dp)
        .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(4.dp)),
        horizontalArrangement = Arrangement.SpaceEvenly,

    ) {
        Text(text = "${persona.nome} ${persona.cognome}", modifier = Modifier.padding(8.dp).weight(2f).align(Alignment.CenterVertically) )
        Text( text = "${persona.ha_pagato}/${persona.ha_partecipato}", modifier = Modifier.padding(8.dp).weight(1f).align(Alignment.CenterVertically))
        Button(onClick = {
            globalContext.setPersonaDaModificare(persona)
            onClickVisualizza( persona )
                         }, modifier = Modifier.padding(8.dp).weight(2f) ) {
            Text(stringResource(Res.string.modifica))
        }
    }
}
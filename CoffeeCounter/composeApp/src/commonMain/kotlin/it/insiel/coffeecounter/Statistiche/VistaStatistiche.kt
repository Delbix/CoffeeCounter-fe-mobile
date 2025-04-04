package it.insiel.coffeecounter.Statistiche

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.insiel.coffeecounter.RichiesteServer.Persona
import it.insiel.coffeecounter.RichiesteServer.Richiesta
import it.insiel.coffeecounter.RichiesteServer.RichiestaDatiService
import it.insiel.coffeecounter.utils.CommonDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * VISTA
 * Gestioe delle statistiche dell'app
 *
 * **Parametri**
 * @param richiestaDati [RichiestaDatiService] = servizio per la ricezione dei dati
 * **Lambda**
 * @param onCloseModal = evento generato dalla chiusura della finestra modale
 *
 * **Funzioni ausiliarie**
 * pagatore = vedi descr funzione
 * bevitoreAccanito = vedi descr funzione
 */

@Composable
fun vistaStatistiche(
    richiestaDati: RichiestaDatiService = Richiesta,
    onCloseModal: () -> Unit
) {
    val scope: CoroutineScope = rememberCoroutineScope()
    var persone by remember { mutableStateOf<List<Persona>>(emptyList()) }
    //parametri per la finestra modale
    var dialogHeader by remember { mutableStateOf( "" ) }
    var dialogHeaderColor by remember { mutableStateOf( Color.Blue ) }
    var dialogMessage by remember { mutableStateOf( "" ) }
    val isDialogOpen = remember { mutableStateOf(false) }
    //loading della pagina --> attesa della richiesta
    var isLoading by remember { mutableStateOf(true) }

    //Al load della pagina
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val result = richiestaDati.fetchPersonas()
                isLoading = false
                persone = result
            } catch (e:Exception){
                isLoading = false
                dialogHeader = "ERRORE"
                dialogHeaderColor = Color.Red
                dialogMessage = "Errore di ricezione dei dati: \n${e.message} "
                isDialogOpen.value = true
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val bevitoreAccanitoAward: Persona = bevitoreAccanito( persone )
        val pagatoreAward: Persona = pagatore( persone )
        Text("Statistiche dell'App", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        if ( isLoading ) {
            CircularProgressIndicator()
        } else {
            if (bevitoreAccanitoAward.id != -1) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = "sideMenu")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Il bevitore accanito è ${bevitoreAccanitoAward.nome} ${bevitoreAccanitoAward.cognome}")
                }
            }
            if (pagatoreAward.id != -1) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = "sideMenu")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "L'utente che ha pagato più volte il caffè è ${pagatoreAward.nome} ${bevitoreAccanitoAward.cognome}")
                }
            }
        }
        //finestra modale per i messaggi di stato
        if ( dialogHeaderColor == Color.Blue ){
            CommonDialog(isDialogOpen.value, dialogMessage, dialogHeader) {
                isDialogOpen.value = false
                onCloseModal() //ritorno alla schermata home
            }
        } else {
            CommonDialog(isDialogOpen.value, dialogMessage, dialogHeader, dialogHeaderColor) {
                isDialogOpen.value = false
                onCloseModal() //ritorno alla schermata home
            }
        }
    }
}

/**
 * Funzione che torna la persona che ha pagato + volte
 * @param persone [List<Persona>] = elenco su cui eseguire l'operazione
 * @return Persona, la persona con ha_partecipato + grande.. se non ci sono persone ritorno una persona con id -1 (ERRORE)
 */
fun pagatore(persone: List<Persona>): Persona {
    var result: Persona = Persona( -1, "","", -1, -1)
    var counter = -1
    persone.forEach { persona: Persona ->
        if ( persona.ha_pagato > counter ){
            result = persona
            counter = persona.ha_pagato
        }
    }
    return result
}

/**
 * Funzione che torna la persona che ha partecipato + volte
 * @param persone [List<Persona>] = elenco su cui eseguire l'operazione
 * @return Persona, la persona con ha_partecipato + grande.. se non ci sono persone ritorno una persona con id -1 (ERRORE)
 */
fun bevitoreAccanito(persone: List<Persona>): Persona {
    var result: Persona = Persona( -1, "","", -1, -1)
    var counter = -1
    persone.forEach { persona: Persona ->
        if ( persona.ha_partecipato > counter ){
            result = persona
            counter = persona.ha_partecipato
        }
    }
    return result
}

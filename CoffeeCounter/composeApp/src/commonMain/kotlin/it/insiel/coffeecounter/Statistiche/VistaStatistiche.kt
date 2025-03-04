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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.insiel.coffeecounter.RichiesteServer.Persona
import it.insiel.coffeecounter.RichiesteServer.Richiesta
import it.insiel.coffeecounter.RichiesteServer.RichiestaDatiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * VISTA
 * delle statistiche dell'app
 *
 * **Funzioni ausiliarie**
 * pagatore = vedi descr funzione
 * bevitoreAccanito = vedi descr funzione
 */

@Composable
fun vistaStatistiche( richiestaDati: RichiestaDatiService = Richiesta) {
    val scope: CoroutineScope = rememberCoroutineScope()
    var persone by remember { mutableStateOf<List<Persona>>(emptyList()) }


    //Al load della pagina
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val result = richiestaDati.fetchPersonas()
                persone = result
            } catch (e:Exception){
                //TODO handle e.message
//                dialogHeader = "ERRORE"
//                dialogMessage = "Errore di ricezione dei dati: \n${e.message} "
//                isDialogOpen.value = true
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
        if ( bevitoreAccanitoAward.id != -1 ) {
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
        if ( pagatoreAward.id != -1 ) {
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

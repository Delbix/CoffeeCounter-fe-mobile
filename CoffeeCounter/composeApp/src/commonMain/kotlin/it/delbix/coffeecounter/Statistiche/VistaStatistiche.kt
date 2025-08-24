package it.delbix.coffeecounter.Statistiche

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coffeecounter.composeapp.generated.resources.Res
import coffeecounter.composeapp.generated.resources.statisticheTitle
import it.delbix.coffeecounter.RichiesteServer.Persona
import it.delbix.coffeecounter.RichiesteServer.Richiesta
import it.delbix.coffeecounter.RichiesteServer.RichiestaDatiService
import it.delbix.coffeecounter.utils.CommonDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

/**
 * VISTA
 * Gestione delle statistiche dell'app
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
    val dataUltimaTransazione = remember { mutableStateOf("") }
    val caffeBevutiTot = remember { mutableStateOf(0) }
    val caffeBevutiMese = remember { mutableStateOf(0) }
    val caffeBevutiSettimana = remember { mutableStateOf(0) }
    //parametri per la finestra modale
    var dialogHeader by remember { mutableStateOf( "" ) }
    var messageType by remember { mutableStateOf( 1 ) }
    var dialogMessage by remember { mutableStateOf( "" ) }
    val isDialogOpen = remember { mutableStateOf(false) }
    //loading della pagina --> attesa della richiesta
    var isLoading by remember { mutableStateOf(true) }

    //Al load della pagina
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val richiestaPersone = richiestaDati.fetchPersonas()
                val richiestaUltimaTransazione = richiestaDati.getDataUltimaTransazione()
                val caffeBevuti = richiestaDati.getCaffeBevuti()
                isLoading = false
                persone = richiestaPersone
                dataUltimaTransazione.value = richiestaUltimaTransazione
                caffeBevutiTot.value = caffeBevuti.day0
                caffeBevutiMese.value = caffeBevuti.Mese
                caffeBevutiSettimana.value = caffeBevuti.Settimana
            } catch (e:Exception){
                isLoading = false
                dialogHeader = "ERRORE"
                messageType = 2
                dialogMessage = "Errore di ricezione dei dati: \n${e.message} "
                isDialogOpen.value = true
                isLoading = false
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
        val bevitoreAccanitoAward: List<Persona> = bevitoreAccanito( persone )
        val pagatoreAward: List<Persona> = pagatore( persone )
        Text(stringResource(Res.string.statisticheTitle), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        if ( isLoading ) {
            CircularProgressIndicator()
        } else {
            //BEVITORE ACCANITO AWARD
            if (bevitoreAccanitoAward.isEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Non è stato ancora bevuto nessun caffè")
                }
            } else if (bevitoreAccanitoAward.size == 1) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Il bevitore accanito è ${bevitoreAccanitoAward.get(0).nome} ${
                            bevitoreAccanitoAward.get(
                                0
                            ).cognome
                        }"
                    )
                }
            } else {
                var listaBevitoriParimerito = ""
                bevitoreAccanitoAward.forEach { persona ->
                    listaBevitoriParimerito += " - " + persona.nome + " " + persona.cognome + "\n"
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Icon(Icons.Default.Favorite, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Gli utenti che hanno bevuto più volte il caffè sono: \n${listaBevitoriParimerito}")
                }
            }

            //PAGATORE AWARD
            if (pagatoreAward.isEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Non è stato pagato ancora nessun caffè!")
                }
            } else if (pagatoreAward.size == 1) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "L'utente che ha pagato più volte il caffè è ${
                            pagatoreAward.get(
                                0
                            ).nome
                        } ${pagatoreAward.get(0).cognome} con ${pagatoreAward.get(0).caffe_pagati} caffè pagati"
                    )
                }
            } else {
                var listaPagatoriParimerito = ""
                var n_caffe = 0
                pagatoreAward.forEach { persona ->
                    listaPagatoriParimerito += " - " + persona.nome + " " + persona.cognome + "\n"
                    n_caffe = persona.caffe_pagati
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Gli utenti che hanno pagato più volte il caffè sono: \n${listaPagatoriParimerito} \nCon $n_caffe caffè pagati")
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .padding(10.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                if ( dataUltimaTransazione.value != "") {
                    Text(
                        text = "Ultima transazione registrata in data " + dataUltimaTransazione.value.substring(
                            0,
                            10
                        )
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .padding(10.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Persone presenti in archivio " + persone.size)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .padding(10.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Totale caffè bevuti: " + caffeBevutiTot.value +
                        "\nDi cui nell'ultimo mese: " + caffeBevutiMese.value +
                        "\nDi cui in questa settimana: " + caffeBevutiSettimana.value )
            }
        }
        //finestra modale per i messaggi di stato
        CommonDialog(isDialogOpen.value, dialogMessage, dialogHeader, messageType) {
            isDialogOpen.value = false
            onCloseModal() //ritorno alla schermata home
        }
    }
}

/**
 * Decreta quale/i persone hanno pagato più volte
 * @param persone [List<Persona>] = elenco su cui eseguire l'operazione
 * @return List<Persona>|emptyList, le persone/a con caffe_pagati + grande
 */
fun pagatore(persone: List<Persona>): List<Persona> {
    val ha_pagatoList = persone.maxOfOrNull { it.caffe_pagati } ?: return emptyList<Persona>()
    return persone.filter { it.caffe_pagati == ha_pagatoList }
}

/**
 * Decreta quale/i persone hanno partecipato più volte
 * @param persone [List<Persona>] = elenco su cui eseguire l'operazione
 * @return List<Persona>|emptyList, la persona/e con ha_partecipato + grande
 */
fun bevitoreAccanito(persone: List<Persona>): List<Persona> {
    val ha_partecipatoList = persone.maxOfOrNull { it.ha_partecipato } ?: return emptyList<Persona>()
    return persone.filter { it.ha_partecipato == ha_partecipatoList }
}

package it.insiel.coffeecounter.Transazioni

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import it.insiel.coffeecounter.RichiesteServer.*
import it.insiel.coffeecounter.utils.CommonDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * MODULO
 * Gestione della tabella di una transazione
 * @required essere chiamato da VistaTransazioniNew
 *
 * **Parametri**
 * @param scope [CoroutineScope] = scope di VistaTransazioniNew
 * @param manuale [Boolean] = abilita scelta manuale di chi paga
 * @param invioDati [InvioDatiService] = oggetto utilizzato per l'invio di dati al server
 * @param richiesteDati [RichiestaDatiService] = oggetto utilizzato per ricevere dati dal server
 * **Lambda**
 * @param onCloseModal = evento generato dalla chiusura della finestra modale
 *
 * **Funzioni ausiliarie**
 * formattaPartecipanti(...) = vedi descr funzione
 */

@Composable
fun TabellaTransazione( scope: CoroutineScope,
                        manuale: Boolean,
                        invioDati: InvioDatiService = InvioDati,
                        richiesteDati: RichiestaDatiService = Richiesta,
                        onCloseModal: () -> Unit ){
    //logMessage("MyComposable is recomposing")
    var valori by remember { mutableStateOf( TransazioniUI() ) }
    //parametri per la finestra modale
    var dialogHeader by remember { mutableStateOf( "" ) }
    var dialogHeaderColor by remember { mutableStateOf( Color.Blue ) }
    var dialogMessage by remember { mutableStateOf( "" ) }
    val isDialogOpen = remember { mutableStateOf(false) }
    val persone = valori.persone
    //loading della pagina --> attesa della richiesta
    var isLoading by remember { mutableStateOf(true) }
    //parametri per il filtro di ricerca
    var query by remember { mutableStateOf("") }
    var showTextField by remember { mutableStateOf(false) }
    val filteredPersons = remember(query, persone) {
        //se il filtro è vuoto li vedo tutti
        if (query == ""){
            persone
        }
        else{
            persone.filter {
                it.nome.contains(query, ignoreCase = true) || it.cognome.contains(query, ignoreCase = true)
            }
        }
    }

    //Al load della pagina -> lo fa una volta sola
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val result = richiesteDati.fetchPersonas()
                isLoading = false
                valori = TransazioniUI( result )
            } catch (e:Exception){
                valori = TransazioniUI( emptyList(), errorMessage = e.message )
                isLoading = false
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if ( isLoading ){
            CircularProgressIndicator()
        } else {
            if (valori.errorMessage != null) {
                //messaggio di errore nel dialog
                dialogHeader = "ERRORE!!"
                dialogHeaderColor = Color.Red
                dialogMessage = "Errore di ricezione dei dati: \n${valori.errorMessage}"
                isDialogOpen.value = true
            }
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { showTextField = !showTextField }, modifier = Modifier.testTag("filterIcon")) {
                    Icon( imageVector = Icons.Default.Search, contentDescription = "Search" )
                }
                if (showTextField) {
                    TextField(
                        value = query,
                        onValueChange = { query = it },
                        label = { Text("Filtra per nome o cognome") },
                        modifier = Modifier.fillMaxWidth().testTag("filterInput")
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(3.dp).height(50.dp)
                            .background(MaterialTheme.colors.primary, RoundedCornerShape(4.dp)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ID",
                            modifier = Modifier.weight(1f).testTag("headerID"),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Nome Cognome",
                            modifier = Modifier.weight(3f).testTag("headerNome"),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Partecipa",
                            modifier = Modifier.weight(1f).testTag("headerPartecipa"),
                            fontWeight = FontWeight.Bold
                        )
                        if (manuale) {
                            Text(
                                text = "Paga",
                                modifier = Modifier.weight(1f).testTag("headerPaga"),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                items(filteredPersons, key = { it.id }) { persona ->
                    RigaPersona(persona, valori, manuale) { updatedValori ->
                        valori = updatedValori.copy()
                    }
                }
            }

            //bottone di invio dei dati
            Button(
                modifier = Modifier.testTag("inviaButton"),
                onClick = {
                    scope.launch {
                        try {
                            if (valori.transazione.pagata_da == null || valori.transazione.partecipanti.isEmpty()) {
                                //se la transazione non è pronta per l'invio
                                dialogHeader = "ERRORE nella creazione della transazione!"
                                dialogHeaderColor = Color.Red
                                dialogMessage =
                                    "La lista dei partecipanti è vuota o manca chi paga! \nNon verrà registrato nulla!"
                                isDialogOpen.value = true
                            } else {
                                //transazione pronta all'invio, quindi facciamo il tentativo
                                valori = valori.copy(
                                    transazione = valori.transazione.copy(
                                        data = Clock.System.now()
                                            .toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
                                    )
                                )
                                val transazioneResponse: Transazione =
                                    invioDati.sendTransazione(valori.transazione)
                                var partecipanti: String =
                                    formattaPartecipanti(transazioneResponse.partecipanti)
                                dialogHeader = "Dati inviati con successo:"
                                dialogHeaderColor = Color.Blue
                                dialogMessage =
                                    "Riepilogo transazione: \nID:${transazioneResponse.id} \nData ${transazioneResponse.data} \nPartecipanti: \n$partecipanti \nPagata da: ${transazioneResponse.pagata_da?.nome} ${transazioneResponse.pagata_da?.cognome}"
                                isDialogOpen.value = true
                            }
                        } catch (e: Exception) {
                            //messaggio di errore nel dialog
                            dialogHeader = "ERRORE!!"
                            dialogHeaderColor = Color.Red
                            dialogMessage = "Errore nell'invio dei dati: \n${e.message}"
                            isDialogOpen.value = true
                        }
                    }
                }) {
                Text("Invia")
            }

            //finestra modale per i messaggi di stato
            if (dialogHeaderColor == Color.Blue) {
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
}



/**
 * Funzione che crea una lista in formato String con nome e cognome
 * @param participanti MutableList<Persona> -> elenco dei partecipanti alla transazione
 * @return String -> elenco numerato dei partecipanti
 */
fun formattaPartecipanti(partecipanti: MutableList<Persona>): String {
    var result:String = ""
    var counter:Int = 1
    partecipanti.forEach { persona: Persona ->
        result += " $counter) ${persona.nome} ${persona.cognome} \n"
        counter ++
    }
    return result
}


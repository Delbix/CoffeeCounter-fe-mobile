package it.insiel.coffeecounter.Transazioni

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.ktor.client.statement.bodyAsText
import it.insiel.coffeecounter.RichiesteServer.Persona
import it.insiel.coffeecounter.RichiesteServer.Transazione
import it.insiel.coffeecounter.RichiesteServer.sendTransazione
import it.insiel.coffeecounter.utils.CommonDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json

/**
 * Gestione della tabella di una transazione
 * Elemento di VistaTransazioniNew
 */

@Composable
fun TabellaTransazione(valoriPadre: ValoriTabellaTransazioni, scope: CoroutineScope, manuale: Boolean,
                       onMyEvent: (ValoriTabellaTransazioni) -> Unit, onCloseModal: () -> Unit){
    var valori by remember { mutableStateOf( valoriPadre.copy() ) }
    valori = valoriPadre.copy() //TODO se lo tolgo non mi visualizza nulla nella tabella?!?
    //parametri per la finestra modale
    var dialogHeader by remember { mutableStateOf( "" ) }
    var dialogHeaderColor by remember { mutableStateOf( Color.Blue ) }
    var dialogMessage by remember { mutableStateOf( "" ) }
    val isDialogOpen = remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (valori.errorMessage != null) {
            //messaggio di errore nel dialog
            dialogHeader = "ERRORE!!"
            dialogHeaderColor = Color.Red
            dialogMessage = "Errore di ricezione dei dati: \n${valori.errorMessage}"
            isDialogOpen.value = true
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(3.dp)
                        .background(Color.Blue, RoundedCornerShape(4.dp))
                ) {
                    Text(text = "ID", modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "Nome Cognome", modifier = Modifier.weight(3f),
                        fontWeight = FontWeight.Bold)
                    Text(text = "Partecipa", modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold)
                    if ( manuale ){
                        Text(text = "Paga", modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.Bold)
                    }
                }
            }
            items(valori.persone) { persona ->
                RigaPersona(persona, valori, manuale){ updatedValori ->
                    valori = updatedValori.copy()
                    onMyEvent(valori)
                }
            }
        }

        //bottone di invio dei dati
        Button(
            onClick = {
                scope.launch {
                    try {
                        if ( valori.transazione.pagata_da == null || valori.transazione.partecipanti.isEmpty() ){
                            //se la transazione non è pronta per l'invio
                            dialogHeader = "ERRORE nella creazione della transazione!"
                            dialogHeaderColor = Color.Red
                            dialogMessage = "La lista dei partecipanti è vuota o manca chi paga! \nNon verrà registrato nulla!"
                            isDialogOpen.value = true
                        } else {
                            //transazione pronta all'invio, quindi facciamo il tentativo
                            valori.transazione.data = Clock.System.now()
                                .toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
                            val response = sendTransazione(valori.transazione)
                            val transazioneResponse: Transazione = Json.decodeFromString<Transazione>(response.bodyAsText())
                            var partecipanti :String = formattaPartecipanti( transazioneResponse.partecipanti )
                            dialogHeader = "Dati inviati con successo:"
                            dialogHeaderColor = Color.Blue
                            dialogMessage = "Riepilogo transazione: \nID:${transazioneResponse.id} \nPartecipanti: \n$partecipanti \nPagata da: ${transazioneResponse.pagata_da?.nome} ${transazioneResponse.pagata_da?.cognome}"
                            isDialogOpen.value = true
                        }
                    } catch (e: Exception) {
                        //messaggio di errore nel dialog
                        dialogHeader = "ERRORE!!"
                        dialogHeaderColor = Color.Red
                        dialogMessage = "Errore nell'invio dei dati: \n${e.message}"
                        isDialogOpen.value = true
                    }
                    onMyEvent(valori)
                }
            }) {
            Text("Invia")
        }

        //finestra modale per i messaggi di stato
        CommonDialog(isDialogOpen.value, dialogMessage, dialogHeader, dialogHeaderColor) {
            isDialogOpen.value = false
            onCloseModal() //ritorno alla schermata home
        }
    }
}


/**
 * Funzione che crea una lista in formato String con nome e cognome
 * @param MutableList<Persona> -> elenco dei partecipanti alla transazione
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


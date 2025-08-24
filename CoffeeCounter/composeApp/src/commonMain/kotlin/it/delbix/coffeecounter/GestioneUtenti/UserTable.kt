package it.delbix.coffeecounter.GestioneUtenti

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coffeecounter.composeapp.generated.resources.Res
import coffeecounter.composeapp.generated.resources.accSearch
import coffeecounter.composeapp.generated.resources.filterPlaceHolder
import coffeecounter.composeapp.generated.resources.modifica
import coffeecounter.composeapp.generated.resources.tableHeaderNome
import coffeecounter.composeapp.generated.resources.tableHeaderPagatoPartecipato
import it.delbix.coffeecounter.RichiesteServer.Persona
import it.delbix.coffeecounter.RichiesteServer.Richiesta
import it.delbix.coffeecounter.RichiesteServer.RichiestaDatiService
import it.delbix.coffeecounter.utils.CommonDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

/**
 * MODULO
 * Gestione della tabella di elenco degli utenti
 * @required essere chimato da VistaUtenti
 *
 * **Parametri**
 * @param richiestaDati [RichiestaDatiService]
 * **Lambda**
 * @param onVisualizzaUtente = per cambiare la vista per la visualizzazione di un utente specifico
 * @param onErrorDetected = per cambiare la vista in caso di errore
 */

@Composable
fun UserTable( richiestaDati: RichiestaDatiService = Richiesta, onVisualizzaUtente: (Persona) -> Unit, onErrorDetected: () -> Unit ) {

    //varibili utilizzate localmente
    var persone by remember { mutableStateOf<List<Persona>>(emptyList()) }
    val scope: CoroutineScope = rememberCoroutineScope()
    //parametri per la finestra modale
    var dialogHeader by remember { mutableStateOf( "" ) }
    val messageType = remember { mutableStateOf( 2 ) } //default colore Rosso per errori
    var dialogMessage by remember { mutableStateOf( "" ) }
    val isDialogOpen = remember { mutableStateOf(false) }
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

    //all'avvio della vista eseguo la chiamata al server per avere la lista di persone presenti
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val result = richiestaDati.fetchPersonas()
                isLoading = false
                persone = result
            } catch (e:Exception){
                isLoading = false
                dialogHeader = "ERRORE"
                dialogMessage = "Errore di ricezione dei dati: \n${e.message} "
                isDialogOpen.value = true
            }
        }
    }


    //VISTA effettiva
    Column(modifier = Modifier.fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if ( isLoading ){
            CircularProgressIndicator()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    IconButton(onClick = { showTextField = !showTextField }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = stringResource(Res.string.accSearch))
                    }
                    if (showTextField) {
                        TextField(
                            value = query,
                            onValueChange = { query = it },
                            label = { Text(stringResource(Res.string.filterPlaceHolder)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(3.dp).testTag("tabellaHeader")
                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp)),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            stringResource(Res.string.tableHeaderNome), modifier = Modifier.padding(8.dp).weight(1f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            stringResource(Res.string.tableHeaderPagatoPartecipato), modifier = Modifier.padding(8.dp).weight(1f),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            stringResource(Res.string.modifica), modifier = Modifier.padding(8.dp).weight(1f),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                items(filteredPersons) { persona ->
                    RigaUtente(persona) { persona ->
                        onVisualizzaUtente(persona)
                    }
                }
            }


            //finestra modale per i messaggi di stato
            CommonDialog(isDialogOpen.value, dialogMessage, dialogHeader, messageType.value) {
                isDialogOpen.value = false
                onErrorDetected()
            }
        }
    }
}




package it.insiel.coffeecounter.GestioneUtenti

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
    var dialogHeaderColor by remember { mutableStateOf( Color.Red ) } //default colore Rosso per errori
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
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(3.dp)
                            .background(MaterialTheme.colors.primary, RoundedCornerShape(4.dp)),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            "Nominativo", modifier = Modifier.padding(8.dp).weight(1f),
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Text(
                            "Pagato/Partecipato", modifier = Modifier.padding(8.dp).weight(1f),
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Text(
                            "Modifica", modifier = Modifier.padding(8.dp).weight(1f),
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
                items(filteredPersons) { persona ->
                    RigaUtente(persona) { persona ->
                        onVisualizzaUtente(persona)
                    }
                }
            }
            IconButton(onClick = { showTextField = !showTextField }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
            if (showTextField) {
                TextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Filtra per nome o cognome") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            //finestra modale per i messaggi di stato
            if (dialogHeaderColor == Color.Blue) {
                CommonDialog(isDialogOpen.value, dialogMessage, dialogHeader) {
                    isDialogOpen.value = false
                    onErrorDetected()
                }
            } else {
                CommonDialog(isDialogOpen.value, dialogMessage, dialogHeader, dialogHeaderColor) {
                    isDialogOpen.value = false
                    onErrorDetected()
                }
            }
        }
    }
}




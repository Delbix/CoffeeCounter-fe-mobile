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
import androidx.compose.material.Text
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

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val result = richiestaDati.fetchPersonas()
                persone = result
            } catch (e:Exception){
                dialogHeader = "ERRORE"
                dialogMessage = "Errore di ricezione dei dati: \n${e.message} "
                isDialogOpen.value = true
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(3.dp)
                        .background(Color.Blue, RoundedCornerShape(4.dp))
                ) {
                    Text("Nominativo", modifier = Modifier.padding(8.dp).weight(1f),
                        fontSize = 16.sp,
                        color = Color.White )
                    Text("Pagato/Partecipato", modifier = Modifier.padding(8.dp).weight(1f),
                        fontSize = 16.sp,
                        color = Color.White )
                    Text("Modifica", modifier = Modifier.padding(8.dp).weight(1f),
                        fontSize = 16.sp,
                        color = Color.White )
                }
            }
            items(persone) { persona ->
                RigaUtente(persona){ persona ->
                    onVisualizzaUtente( persona )
                }
            }
        }

        CommonDialog(isDialogOpen.value, dialogMessage, dialogHeader, dialogHeaderColor) {
            isDialogOpen.value = false
            onErrorDetected()
        }
    }
}




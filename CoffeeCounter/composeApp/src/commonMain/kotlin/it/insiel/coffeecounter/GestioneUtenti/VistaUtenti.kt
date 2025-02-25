package it.insiel.coffeecounter.GestioneUtenti

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.insiel.coffeecounter.RichiesteServer.Persona

/**
 * VISTA
 * della gestione utenti
 *
 * onClickAddUser : viene gestito da App.kt per cambiare la vista
 * onVisualizzaUtente : viene gestito da App.kt per cambiare la vista
 */

@Composable
fun VistaUtenti( onClickAddUser: () -> Unit, onVisualizzaUtente: (Persona) -> Unit ) {

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { onClickAddUser() }) {
            Text("Aggiungi Utente")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Elenco Utenti", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        userTable(){ persona ->
            onVisualizzaUtente( persona )
        }
    }
}
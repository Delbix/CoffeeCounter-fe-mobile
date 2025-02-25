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
 * **Parametri**
 *
 * **Lambda**
 * @param onClickAddUser = viene gestito da App.kt per cambiare la vista
 * @param onVisualizzaUtente = viene gestito da App.kt per cambiare la vista per la visualizzazione di un utente specifico
 * @param onErrorDetected = viene gestito da App.kt per cambiare la vista in caso di errore
 */

@Composable
fun VistaUtenti( onClickAddUser: () -> Unit, onVisualizzaUtente: (Persona) -> Unit, onErrorDetected: () -> Unit ) {

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
        UserTable(
            onVisualizzaUtente = { persona ->
                onVisualizzaUtente( persona )
            },
            onErrorDetected = onErrorDetected
        )
    }
}
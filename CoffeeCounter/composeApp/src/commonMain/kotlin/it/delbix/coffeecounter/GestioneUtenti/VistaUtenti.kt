package it.delbix.coffeecounter.GestioneUtenti

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coffeecounter.composeapp.generated.resources.Res
import coffeecounter.composeapp.generated.resources.addUserButton
import coffeecounter.composeapp.generated.resources.elencoUtentiTitle
import it.delbix.coffeecounter.RichiesteServer.Persona
import org.jetbrains.compose.resources.stringResource

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
fun VistaUtenti( paddingValues: PaddingValues ,onClickAddUser: () -> Unit, onVisualizzaUtente: (Persona) -> Unit, onErrorDetected: () -> Unit ) {

    Column(
        modifier = Modifier.fillMaxWidth().padding(paddingValues),//padding(16.dp).padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onClickAddUser() }) {
            Text(stringResource(Res.string.addUserButton), modifier = Modifier.testTag("addUser") )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(stringResource(Res.string.elencoUtentiTitle), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        UserTable(
            onVisualizzaUtente = { persona ->
                onVisualizzaUtente( persona )
            },
            onErrorDetected = onErrorDetected
        )
    }
}
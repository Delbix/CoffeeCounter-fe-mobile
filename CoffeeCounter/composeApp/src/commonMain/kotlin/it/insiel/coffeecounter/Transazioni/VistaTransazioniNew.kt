package it.insiel.coffeecounter.Transazioni

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
import it.insiel.coffeecounter.RichiesteServer.Persona
import it.insiel.coffeecounter.RichiesteServer.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * VISTA
 * della creazione di una transazione
 *
 * **Parametri**
 * @param manuale [Boolean] = abilita scelta manuale di chi paga
 * **Lambda**
 * @param onSuccesfullySend = viene gestito da App.kt per cambiare la vista quando ho inviato i dati al server
 */

@Composable
fun VistaTransazioniNew( manuale: Boolean = false ,onSuccesfullySend: () -> Unit ){
    //TODO applicare il viewModel come descritto https://medium.com/@rowaido.game/managing-ui-states-in-jetpack-compose-7eb15e4f6931
    //IL PROBLEMA è che viewModel: ViewModelTransazioni = viewModel() non mi riconosce la func viewModel()
    val scope: CoroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabellaTransazione( scope, manuale,
            onCloseModal = {
                onSuccesfullySend()
            }
        )
    }

}
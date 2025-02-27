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
import it.insiel.coffeecounter.RichiesteServer.fetchPersonas
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
    var persone by remember { mutableStateOf<List<Persona>>(emptyList()) }

    //mutableStateOf mi fa in modo che ogni volta che modifico i l'elemento mi faccia cambiare tutti gli elementi dell'interfaccia collegati
    //quindi --> devo ricreare una l'oggetto in questione ogni volta che lo voglio modificare.
    //funzione interessante --> valori = valori.copy() mi copia l'oggetto precedente e fa un override solo dei valori passati come parametro al copy
    var valori by remember { mutableStateOf( TransazioniUI( persone ) ) }

    //Al load della pagina -> lo fa una volta sola
    LaunchedEffect(Unit) {
        scope.launch {
            when (val result = fetchPersonas()) {
                is Result.Success -> {
                    valori = TransazioniUI(  result.data )
                }
                is Result.Error -> {
                    valori = TransazioniUI(  emptyList(), errorMessage = result.message )
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TabellaTransazione( valori, scope, manuale,
            onMyEvent = {
                newValori ->
                valori = newValori
            },
            onCloseModal = {
                onSuccesfullySend()
            }
        )
    }

}
package it.delbix.coffeecounter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coffeecounter.composeapp.generated.resources.Res
import coffeecounter.composeapp.generated.resources.caffeAutomatico
import coffeecounter.composeapp.generated.resources.caffeManuale
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.compose.resources.stringResource

/**
 * VISTA
 * vista della schermata home
 *
 * **Parametri**
 *
 * **Lambda**
 * @param onButtonAutomaticoClick = viene gestito da App.kt per cambiare la vista
 * @param onButtonManualeClick = viene gestito da App.kt per cambiare la vista
 */

@Composable
fun VistaMain(onButtonAutomaticoClick: () -> Unit, onButtonManualeClick: () -> Unit, scope: CoroutineScope = rememberCoroutineScope() ) {

    //abilitare il bottone della notifica
    val isEnabled = remember { mutableStateOf(true) }
    val scope: CoroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onButtonAutomaticoClick,
            modifier = Modifier
                .width(250.dp)
                .height(150.dp)
                .padding(8.dp)
                .testTag("automatico")
        ) {
            Text(stringResource(Res.string.caffeAutomatico))
        }
        Button(
            onClick = onButtonManualeClick,
            modifier = Modifier
                .width(250.dp)
                .height(150.dp)
                .padding(8.dp)
                .testTag("manuale")
        ) {
            Text(stringResource(Res.string.caffeManuale))
        }
        //TODO bottone per l'invio della notifica di raccolta compagni!!
        //TODO CRASHA SE LO USO -->Da verificare se manda la notifica almeno.. sembra di si
        //TODO gestire l'errore nel catch meglio.
//        Button(
//            onClick = {
//                scope.launch {
//                    isEnabled.value = false
//                    val notifica = Notifica(
//                        "Il caffè chiama",
//                        "Sei stato chiamato per la pausa caffè",
//                        "notifications"
//                    )
//                    try {
//                        val resp = InvioDati.inviaNotifica(notifica)
//                        Logger.log(resp)
//                    } catch ( e: Exception ){
//                        Logger.log("Qualcosa è andato storto nelle notifiche -> ${e.message}")
//                        e.printStackTrace()
//                    }
//                }
//                Logger.log("out")
//            },
//            modifier = Modifier,
//            enabled = isEnabled.value,
//            shape = MaterialTheme.shapes.medium,
//        ){
//            Text("Chiamata a raccolata")
//        }
    }
}
package it.insiel.coffeecounter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
fun VistaMain(onButtonAutomaticoClick: () -> Unit, onButtonManualeClick: () -> Unit ) {
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
                .width(200.dp)
                .height(100.dp)
                .padding(8.dp)
        ) {
            Text("Caffè automatico")
        }
        Button(
            onClick = onButtonManualeClick,
            modifier = Modifier
                .width(200.dp)
                .height(100.dp)
                .padding(8.dp)
        ) {
            Text("Caffè manuale")
        }
    }
}
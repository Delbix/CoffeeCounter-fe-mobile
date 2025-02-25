package it.insiel.coffeecounter.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 * COMPONENTE GENERICO
 * Finestra di dialogo per messaggi generici
 *
 * **Parametri**
 * @param isDialogOpen [Boolean] = visibilità del componente
 * @param messaggio [String] = messaggio da visualizzare
 * @param header [String] = titolo della finestra [default=titolo di default]
 * @param headerColor [Color] = colore di sfondo del titolo della finestra [default=colore di default]
 * **Lambda**
 * @param onDismissRequest = evento di chiusura del dialog
 */

@Composable
fun CommonDialog(isDialogOpen: Boolean, messaggio: String, header: String = "ModalDialog", headerColor: Color = Color.Blue, onDismissRequest: () -> Unit) {
    if (isDialogOpen) {
        Dialog(onDismissRequest = onDismissRequest) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        header, modifier = Modifier.fillMaxWidth()
                            .background(headerColor)
                            .padding(10.dp),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(messaggio, modifier = Modifier.padding(10.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onDismissRequest) {
                        Text("OK")
                    }
                }
            }
        }
    }
}
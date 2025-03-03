package it.insiel.coffeecounter.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/**
 * DIALOG
 * Conferma di eseguire un operazione
 * onConfirm -> azione da eseguire se viene confermata
 * onDismiss -> azione da eseguire se non viene confermata
 */

/**
 * COMPONENTE GENERICO
 * Finestra di dialogo per la conferma di un operazione
 *
 * **Parametri**
 * @param isDialogOpen [Boolean] = visibilità del componente
 * @param messaggio [String] = messaggio da visualizzare [default=messaggio di default]
 * @param header [String] = titolo della finestra [default=titolo di default]
 * @param headerColor [Color] = colore di sfondo del titolo della finestra [default=colore di default]
 * **Lambda**
 * @param onConfirm = evento di chiusura con conferma
 * @param onDismiss = evento di chiusura senza conferma
 */

@Composable
fun ConfirmDialog( isDialogOpen: Boolean, messaggio: String = "Sei sicuro?",
                   header: String = "ModalDialog", headerColor: Color = Color.Blue,
                   onConfirm: () -> Unit,
                   onDismiss: () -> Unit){
    if (isDialogOpen) {
        Dialog(onDismissRequest = onDismiss) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .testTag("modalDialogConfirm")
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                            .background(headerColor)
                            .padding(10.dp)
                    ){
                        Icon( Icons.Default.Warning, contentDescription = "warning icon" )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = header)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(messaggio, modifier = Modifier.padding(10.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        modifier = Modifier.testTag("conferma"),
                        onClick = onConfirm
                    ) {
                        Text("Conferma")
                    }
                    Button(
                        modifier = Modifier.testTag("annulla"),
                        onClick = onDismiss
                    ) {
                        Text("Annulla")
                    }
                }
            }
        }
    }

}
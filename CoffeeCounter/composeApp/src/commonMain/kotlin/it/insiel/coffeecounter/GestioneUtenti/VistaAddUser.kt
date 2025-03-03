package it.insiel.coffeecounter.GestioneUtenti

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.ktor.client.statement.bodyAsText
import it.insiel.coffeecounter.RichiesteServer.Persona
import it.insiel.coffeecounter.RichiesteServer.InvioDati
import it.insiel.coffeecounter.utils.CommonDialog
import kotlinx.coroutines.launch
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.testTag
import kotlinx.serialization.json.Json

/**
 * VISTA
 * gestisce l'inserimento di un nuovo utente
 *
 * **Parametri**
 *
 * **Lambda**
 * @param onCloseModal = viene gestito da App.kt per cambiare la vista
 */

@Composable
fun VistaAddUser( onCloseModal: () -> Unit ) {
    var nome by remember { mutableStateOf("") }
    var cognome by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf( "" ) }
    val shakeAnim = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    //parametri per la finestra modale
    var dialogHeader by remember { mutableStateOf( "" ) }
    var dialogHeaderColor by remember { mutableStateOf( Color.Blue ) }
    var dialogMessage by remember { mutableStateOf( "" ) }
    val isDialogOpen = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Aggiungi un utente al database")
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.testTag("nome"),
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome ") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.testTag("cognome"),
            value = cognome,
            onValueChange = { cognome = it },
            label = { Text("Cognome ") }
        )
        Button(
            modifier = Modifier.testTag("addUser"),
            onClick = {
            scope.launch {
                if ( nome == "" ){
                    errorMsg = "Il campo Nome deve essere valorizzato"
                    //effetto shake
                    scope.launch {
                        shakeAnim.animateTo(
                            targetValue = 1f,
                            animationSpec = repeatable(
                                iterations = 3,
                                animation = tween(durationMillis = 100, easing = LinearEasing),
                                repeatMode = RepeatMode.Reverse
                            )
                        )
                        shakeAnim.snapTo(0f)
                    }
                }else{
                    try {
                        val persona = Persona(0, nome, cognome, 0, 0, false)
                        val response = InvioDati.sendPersona(persona)
                        val personaResponse: Persona = Json.decodeFromString<Persona>(response.bodyAsText())
                        dialogHeader = "Dati inviati con successo"
                        dialogHeaderColor = Color.Blue
                        dialogMessage = "Riepilogo: \nID:${personaResponse.id} \nNome:${personaResponse.nome} \nCognome:${personaResponse.cognome}"
                        isDialogOpen.value = true
                    } catch (e: Exception) {
                        dialogHeader = "Errore nell'invio dei dati!!"
                        dialogHeaderColor = Color.Red
                        dialogMessage = "${e.message} \nRiprova!"
                        isDialogOpen.value = true
                    }
                }
            }
        }) {
            Text("Aggiungi")
        }

        if (errorMsg != "") {
            Text(text = errorMsg, color = Color.Red, modifier = Modifier.padding(8.dp).scale(1f + shakeAnim.value * 0.05f).testTag("error"))
        }

        CommonDialog(isDialogOpen.value, dialogMessage, dialogHeader, dialogHeaderColor) {
            isDialogOpen.value = false
            onCloseModal()
        }
    }
}
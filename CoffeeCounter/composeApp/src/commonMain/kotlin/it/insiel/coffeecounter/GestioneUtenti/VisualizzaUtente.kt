package it.insiel.coffeecounter.GestioneUtenti

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import it.insiel.coffeecounter.RichiesteServer.Persona
import it.insiel.coffeecounter.RichiesteServer.InvioDati
import it.insiel.coffeecounter.RichiesteServer.InvioDatiService
import it.insiel.coffeecounter.utils.CommonDialog
import it.insiel.coffeecounter.utils.ConfirmDialog
import kotlinx.coroutines.launch

/**
 * VISTA
 * Visualizza/modifica/elimina utente specifico
 *
 * **Parametri**
 * @param persona [Persona] = utente oggetto di visualizzazione
 * @param invioDati [InvioDatiService] = oggetto utilizzato per l'invio di dati al server
 * **Lambda**
 * @param onCloseModal = viene gestito da App.kt per cambiare la vista
 */

@Composable
fun VisualizzaUtente( persona: Persona, invioDati: InvioDatiService = InvioDati, onCloseModal: () -> Unit ){
    var nome by remember { mutableStateOf(persona.nome ) }
    var cognome by remember { mutableStateOf(persona.cognome ) }
    var errorMsg by remember { mutableStateOf( "" ) }
    val shakeAnim = remember { Animatable(0f) }
    val triggerAnim = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    //parametri per la finestra modale
    var dialogHeader by remember { mutableStateOf( "" ) }
    var dialogHeaderColor by remember { mutableStateOf( Color.Blue ) }
    var dialogMessage by remember { mutableStateOf( "" ) }
    val isDialogOpenCommon = remember { mutableStateOf(false) }
    val isDialogOpenConfirm = remember { mutableStateOf(false) }

    //viene eseguita solo quando il triggerAnim cambia valore
    LaunchedEffect(triggerAnim.value){
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Modifica utente")
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
            label = { Text("Nome ") }
        )
        Button(
            modifier = Modifier.testTag("modifica"),
            onClick = {
            scope.launch {
                if (nome == "") {
                    errorMsg = "Il campo Nome deve essere valorizzato"
                    triggerAnim.value = !triggerAnim.value
                } else {
                    if ( persona.nome == nome && persona.cognome == cognome ){
                        errorMsg = "Non hai modificato nulla!"
                        triggerAnim.value = !triggerAnim.value
                    } else {
                        try {
                            val personaMod = Persona(
                                persona.id,
                                nome,
                                cognome,
                                persona.ha_pagato,
                                persona.ha_partecipato,
                                false
                            )
                            val personaResponse:Persona = invioDati.sendPersona(personaMod)
                            dialogHeader = "Dati modificati con successo:"
                            dialogHeaderColor = Color.Blue
                            dialogMessage =
                                "Nome: ${persona.nome} --> ${personaResponse.nome} \nCognome: ${persona.cognome} --> ${personaResponse.cognome}"
                        } catch (e: Exception) {
                            dialogHeader = "Errore nell'invio dei dati:"
                            dialogHeaderColor = Color.Red
                            dialogMessage = "${e.message}"
                        }
                        isDialogOpenCommon.value = true
                    }
                }
            }
        }) {
            Text("Salva modifiche")
        }

        Button(
            modifier = Modifier.testTag("elimina"),
            onClick = {
            isDialogOpenConfirm.value = true
        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)) {
            Text("Elimina" )
        }

        if (errorMsg != "") {
            Text(text = errorMsg, color = Color.Red, modifier = Modifier.padding(8.dp).scale(1f + shakeAnim.value * 0.05f).testTag("error") )
        }

        CommonDialog(isDialogOpenCommon.value, dialogMessage, dialogHeader, dialogHeaderColor) {
            isDialogOpenCommon.value = false
            onCloseModal()
        }

        ConfirmDialog(
            isDialogOpenConfirm.value,
            messaggio = "Sei sicuro di voler eliminare ${persona.nome} ${persona.cognome}?",
            header = "Attenzione",
            headerColor = Color.Yellow,
            onConfirm = {
                scope.launch {
                    try {
                        val response = invioDati.eliminaPersona(persona)
                        if ( response == "true" ){
                            dialogHeader = "Eliminazione eseguita con successo!"
                            dialogHeaderColor = Color.Blue
                            dialogMessage = "${persona.nome} ${persona.cognome} è stato eliminato!"
                        } else {
                            dialogHeader = "ERRORE!!"
                            dialogHeaderColor = Color.Red
                            dialogMessage = "C'è stato un errore durante l'eliminazione, riprova!"
                        }
                        isDialogOpenCommon.value = true
                    } catch (e: Exception) {
                        dialogHeader = "Errore nell'invio dei dati:"
                        dialogHeaderColor = Color.Red
                        dialogMessage = e.message!!
                    }
                    isDialogOpenConfirm.value = false
                    isDialogOpenCommon.value = true
                }
            }
        ){
            isDialogOpenConfirm.value = false
        }
    }
}
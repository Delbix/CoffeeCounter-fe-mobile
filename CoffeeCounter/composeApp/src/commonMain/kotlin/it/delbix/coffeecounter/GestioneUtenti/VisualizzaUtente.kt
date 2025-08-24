package it.delbix.coffeecounter.GestioneUtenti

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import coffeecounter.composeapp.generated.resources.Res
import coffeecounter.composeapp.generated.resources.delete
import coffeecounter.composeapp.generated.resources.editUser
import coffeecounter.composeapp.generated.resources.nameLabel
import coffeecounter.composeapp.generated.resources.saveEdit
import coffeecounter.composeapp.generated.resources.surnameLabel
import it.delbix.coffeecounter.GlobalContext
import it.delbix.coffeecounter.RichiesteServer.InvioDati
import it.delbix.coffeecounter.RichiesteServer.InvioDatiService
import it.delbix.coffeecounter.RichiesteServer.Persona
import it.delbix.coffeecounter.utils.CommonDialog
import it.delbix.coffeecounter.utils.ConfirmDialog
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

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
fun VisualizzaUtente( invioDati: InvioDatiService = InvioDati, onCloseModal: () -> Unit ){
    //viewModel
    val globalContext: GlobalContext = koinViewModel<GlobalContext>()
//    var nome by remember { mutableStateOf(persona.nome ) }
    var nome by remember { mutableStateOf( globalContext.currentPersonaDaModificare.value.nome ) }
    var cognome by remember { mutableStateOf( globalContext.currentPersonaDaModificare.value.cognome ) }
    var errorMsg by remember { mutableStateOf( "" ) }
    val shakeAnim = remember { Animatable(0f) }
    val triggerAnim = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    //parametri per la finestra modale
    var dialogHeader by remember { mutableStateOf( "" ) }
    var messageType by remember { mutableStateOf( 1 ) }
    var dialogMessage by remember { mutableStateOf( "" ) }
    val isDialogOpenCommon = remember { mutableStateOf(false) }
    val isDialogOpenConfirm = remember { mutableStateOf(false) }
    //abilitare i bottoni
    val isEnabled = remember { mutableStateOf(true) }

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
        isEnabled.value = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(Res.string.editUser))
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.testTag("nome"),
            value = nome,
            onValueChange = { nome = it },
            label = { Text(stringResource(Res.string.nameLabel)) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.testTag("cognome"),
            value = cognome,
            onValueChange = { cognome = it },
            label = { Text(stringResource(Res.string.surnameLabel)) }
        )
        Button(
            modifier = Modifier.testTag("modifica"),
            enabled = isEnabled.value,
            onClick = {
                isEnabled.value = false
                scope.launch {
                    if (nome == "") {
                        errorMsg = "Il campo Nome deve essere valorizzato"
                        triggerAnim.value = !triggerAnim.value
                    } else {
                        if ( globalContext.currentPersonaDaModificare.value.nome == nome && globalContext.currentPersonaDaModificare.value.cognome == cognome ){
                            errorMsg = "Non hai modificato nulla!"
                            triggerAnim.value = !triggerAnim.value
                        } else {
                            try {
                                val personaMod = Persona(
                                    globalContext.currentPersonaDaModificare.value.id,
                                    nome,
                                    cognome,
                                    globalContext.currentPersonaDaModificare.value.ha_pagato,
                                    globalContext.currentPersonaDaModificare.value.ha_partecipato,
                                    globalContext.currentPersonaDaModificare.value.caffe_pagati,
                                    false
                                )
                                val personaResponse:Persona = invioDati.sendPersona(personaMod)
                                if ( personaResponse.id == -2 ){
                                    throw Exception( "Non è possibile rinominare questa persona con un nome già presente in archivio! \nLa persona ${personaResponse.nome} ${personaResponse.cognome} è già presente nel database \n" )
                                }
                                dialogHeader = "Dati modificati con successo:"
                                messageType = 1
                                dialogMessage =
                                    "Nome: ${globalContext.currentPersonaDaModificare.value.nome} --> ${personaResponse.nome} \nCognome: ${globalContext.currentPersonaDaModificare.value.cognome} --> ${personaResponse.cognome}"
                            } catch (e: Exception) {
                                dialogHeader = "Errore nell'invio dei dati:"
                                messageType = 2
                                dialogMessage = "${e.message}"
                            }
                            isDialogOpenCommon.value = true
                        }
                    }
                }
        }) {
            Text(stringResource(Res.string.saveEdit))
        }

        Button(
            modifier = Modifier.testTag("elimina"),
            enabled = isEnabled.value,
            onClick = {
                isEnabled.value = false
                isDialogOpenConfirm.value = true
            },
            colors = ButtonDefaults.filledTonalButtonColors( MaterialTheme.colorScheme.error )) {
            Text( stringResource(Res.string.delete) )
        }

        if (errorMsg != "") {
            Text(text = errorMsg, color = Color.Red, modifier = Modifier.padding(8.dp).scale(1f + shakeAnim.value * 0.05f).testTag("error") )
        }

        ///finestra modale
        CommonDialog(isDialogOpenCommon.value, dialogMessage, dialogHeader, messageType) {
            isDialogOpenCommon.value = false
            onCloseModal()
        }


        ConfirmDialog(
            isDialogOpenConfirm.value,
            messaggio = "Sei sicuro di voler eliminare ${globalContext.currentPersonaDaModificare.value.nome} ${globalContext.currentPersonaDaModificare.value.cognome}?",
            header = "Attenzione",
            messageType = 3,
            onConfirm = {
                scope.launch {
                    try {
                        val response = invioDati.eliminaPersona(globalContext.currentPersonaDaModificare.value)
                        if ( response == "true" ){
                            dialogHeader = "Eliminazione eseguita con successo!"
                            messageType = 1
                            dialogMessage = "${globalContext.currentPersonaDaModificare.value.nome} ${globalContext.currentPersonaDaModificare.value.cognome} è stato eliminato!"
                        } else {
                            dialogHeader = "ERRORE!!"
                            messageType = 2
                            dialogMessage = "C'è stato un errore durante l'eliminazione, riprova!"
                        }
                        isDialogOpenCommon.value = true
                    } catch (e: Exception) {
                        dialogHeader = "Errore nell'invio dei dati:"
                        messageType = 2
                        dialogMessage = e.message!!
                    }
                    isDialogOpenConfirm.value = false
                    isDialogOpenCommon.value = true
                }
            }
        ){
            isDialogOpenConfirm.value = false
            isEnabled.value = true
        }
    }
}
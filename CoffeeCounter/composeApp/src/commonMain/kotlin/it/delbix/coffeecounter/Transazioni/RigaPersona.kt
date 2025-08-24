package it.delbix.coffeecounter.Transazioni

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import it.delbix.coffeecounter.RichiesteServer.Persona
import it.delbix.coffeecounter.Settings.CriterioAutomaticoManager
import it.delbix.coffeecounter.Settings.CriterioAutomaticoManagerType

/**
 * MODULO
 * gestisce una riga di tipo persona
 * @Require di essere chiamato da TabellaTransazione
 *
 * **Parametri**
 * @param persona [Persona] = persona oggetto di questa riga
 * @param valoriPadre [TransazioniUI] = valori passati da TabellaTransazioni
 * @param manuale [Boolean] = abilita scelta manuale di chi paga
 * **Lambda**
 * @param onMyEvent = evento che aggiorna i valori di ValoriTabellaTransazioni su chi chiama questo elemento
 */

@Composable
fun RigaPersona(persona: Persona,
                valoriPadre: TransazioniUI,
                manuale: Boolean,
                criterioAutomaticoManager: CriterioAutomaticoManagerType = CriterioAutomaticoManager,
                onMyEvent: (TransazioniUI) -> Unit )
{
    //fa in modo che si aggiorni la riga
    val selectedModalita = remember { mutableStateOf( criterioAutomaticoManager.recuperaCriterioAutomatico() ) }
    val partecipa = remember { mutableStateOf(persona.checked) }
    var ratePersona: Double = 0.0 //per evitare un errore di divisione per 0
    var valori by remember { mutableStateOf( valoriPadre.copy() ) }
    valori = valoriPadre.copy()
    val paga by derivedStateOf { persona.id == valori.transazione.pagata_da?.id } //tiene conto se gli elementi cambiano
    var rate = valori.rate
    if (manuale) { //setto il rate a 0 in modalità manuale così non interferisce
        rate = 0.0
    } else if ( !manuale && selectedModalita.value == 1 ){
        valori = valori.copy( rate = Double.MAX_VALUE )
    }
    //per poter modificare la transazione
    var pagata_da: Persona? = valori.transazione.pagata_da
    var partecipanti: MutableList<Persona> = valori.transazione.partecipanti //è var perchè voglio usare la funzione add()

    if ( persona.ha_partecipato != 0 ){
        if (selectedModalita.value == 1){
            ratePersona = persona.caffe_pagati.toDouble()
        } else if (selectedModalita.value == 2) {
            ratePersona = persona.ha_pagato.toDouble() / persona.ha_partecipato.toDouble()
        }
    }
    //se la persona partecipa ed il suo rate è < di quello attuale ==> aggiorna valori
    if ( partecipa.value && ratePersona < rate ){
        valori = valori.copy( rate = ratePersona, transazione = valori.transazione.copy( pagata_da = persona ) )
        onMyEvent(valori)
    }

    var backgroundColor = if (partecipa.value && valori.transazione.pagata_da == persona) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.secondaryContainer

    Row(modifier = Modifier.fillMaxWidth()//.padding(3.dp)
        .background(backgroundColor, RoundedCornerShape(4.dp)),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = persona.id.toString(),
            modifier = Modifier.weight(1f)
                .padding(4.dp)
                .align(Alignment.CenterVertically)
                .testTag( persona.id.toString() )
        )
        Text(text = "${persona.nome} ${persona.cognome} - $ratePersona",
            modifier = Modifier.weight(3f)
                .align(Alignment.CenterVertically)
                .testTag("${persona.nome}${persona.cognome}")
        )
        //Checkbox di partecipazione
        Checkbox(
            checked = partecipa.value,
            onCheckedChange = { check ->
                //logica del cambio di stato
                partecipa.value = check
                persona.checked = check
                if (partecipa.value) {
                    partecipanti.add( persona )

                    if ( !manuale && (ratePersona < rate) ){ //se sono in modalità automatica e viene soddisfatta la condizione di pagamento
                        pagata_da = persona
                        valori = valori.copy(
                            transazione = valori.transazione.copy( partecipanti = partecipanti, pagata_da = pagata_da ),
                            rate = ratePersona
                        )
                    }else{ //altrimenti aggiungo solo i partecipanti
                        valori = valori.copy( transazione = valori.transazione.copy( partecipanti = partecipanti, pagata_da = pagata_da ), persone = valori.persone )
                    }

                    onMyEvent( valori )
                } else {
                    partecipanti.remove( persona )
                    if ( pagata_da == persona ){
                        pagata_da = null
                        //imposto un rate assurdo per forzare i ricalcoli
                        rate = Double.MAX_VALUE
                    }
                    valori = valori.copy( transazione = valori.transazione.copy( partecipanti = partecipanti, pagata_da = pagata_da ), rate = rate )
                    onMyEvent(valori)
                }
            },
            modifier = Modifier.weight(1f).testTag("check${persona.id}")
        )
        if ( manuale ) {
            //checkbox di pagamento (visibile solo se siamo in modalità manuale)
            Checkbox(
                checked = paga,
                onCheckedChange = { check ->
                    //logica del cambio di stato
                    if ( !partecipa.value ) { //se non è spuntato il partecipa lo forzo (non posso pagare se non partecipo)
                        partecipanti.add( persona )
                        persona.checked = true
                        partecipa.value  = true
                    }
                    if (check) {
                        pagata_da = persona
                        valori = valori.copy( transazione = valori.transazione.copy( partecipanti = partecipanti, pagata_da = pagata_da ) )
                        onMyEvent(valori)
                    } else {
                        pagata_da = null
                        valori = valori.copy( transazione = valori.transazione.copy( partecipanti = partecipanti, pagata_da = pagata_da ) )
                        onMyEvent(valori)
                    }
                },
                modifier = Modifier.weight(1f).testTag("${persona.id}Paga")
            )
        }
    }
    HorizontalDivider(thickness = 1.dp, color = Color.Gray)
}
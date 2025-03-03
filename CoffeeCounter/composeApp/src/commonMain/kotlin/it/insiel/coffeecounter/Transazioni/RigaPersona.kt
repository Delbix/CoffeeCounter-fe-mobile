package it.insiel.coffeecounter.Transazioni

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import it.insiel.coffeecounter.RichiesteServer.Persona

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
fun RigaPersona(persona: Persona, valoriPadre: TransazioniUI, manuale: Boolean,
                onMyEvent: (TransazioniUI) -> Unit ){
    //fa in modo che si aggiorni la riga
    var partecipa by remember { mutableStateOf(persona.checked) }
    var ratePersona: Double = 0.0 //per evitare un errore di divisione per 0
    var valori by remember { mutableStateOf( valoriPadre.copy() ) }
    valori = valoriPadre.copy()
    var paga by remember { mutableStateOf( persona.id == valori.transazione.pagata_da?.id) }
    var rate = valori.rate
    //per poter modificare la transazione
    var pagata_da: Persona? = valori.transazione.pagata_da
    var partecipanti: MutableList<Persona> = valori.transazione.partecipanti //è var perchè voglio usare la funzione add()

    if ( persona.ha_partecipato != 0 ){
        ratePersona = persona.ha_pagato.toDouble() / persona.ha_partecipato.toDouble()
    }
    //se la persona partecipa ed il suo rate è < di quello attuale ==> aggiorna valori
    if ( partecipa && ratePersona < rate ){
        valori = valori.copy( rate = ratePersona, transazione = valori.transazione.copy( pagata_da = persona ) )
        onMyEvent(valori)
    }

    var backgroundColor = if (partecipa && valori.transazione.pagata_da == persona) Color.Green else Color.LightGray

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
            checked = partecipa,
            onCheckedChange = { check ->
                //logica del cambio di stato
                partecipa = check
                persona.checked = check
                if (partecipa) {
                    partecipanti.add( persona )

                    if ( ratePersona < rate ) {
                        if ( !manuale ) { //vado a settare chi paga solo se sono in modalità automatica
                            pagata_da = persona
                        }
                        valori = valori.copy(
                            transazione = valori.transazione.copy( partecipanti = partecipanti, pagata_da = pagata_da ),
                            rate = ratePersona
                        )
                        backgroundColor = Color.Green
                    } else {
                        valori = valori.copy( transazione = valori.transazione.copy( partecipanti = partecipanti, pagata_da = pagata_da ), persone = valori.persone )
                    }
                    onMyEvent( valori )
                } else {
                    partecipanti.remove( persona )
                    if ( pagata_da == persona ){
                        pagata_da = null
                        //imposto un rate assurdo per forzare i ricalcoli
                        rate = 2.0
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
                    if ( !partecipa ) { //se non è spuntato il partecipa lo forzo (non posso pagare se non partecipo)
                        partecipa = true
                    }
                    paga = check
                    if (paga) {
                        pagata_da = persona
                        valori = valori.copy( transazione = valori.transazione.copy( partecipanti = partecipanti, pagata_da = pagata_da ) )
                        onMyEvent(valori)
                    } else {
                        pagata_da = pagata_da
                        valori = valori.copy( transazione = valori.transazione.copy( partecipanti = partecipanti, pagata_da = pagata_da ) )
                        onMyEvent(valori)
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
    Divider(color = Color.Gray, thickness = 1.dp)
}
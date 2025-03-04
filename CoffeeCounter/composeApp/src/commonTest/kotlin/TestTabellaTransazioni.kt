import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.mockk.coEvery
import io.mockk.mockk
import it.insiel.coffeecounter.RichiesteServer.InvioDatiService
import it.insiel.coffeecounter.RichiesteServer.Persona
import it.insiel.coffeecounter.RichiesteServer.RichiestaDatiService
import it.insiel.coffeecounter.RichiesteServer.Transazione
import it.insiel.coffeecounter.Transazioni.TabellaTransazione
import kotlinx.coroutines.CoroutineScope
import kotlin.test.Test
import kotlin.test.assertEquals


class TestTabellaTransazioni {


    /**
     * Testo che la tabella venga visualizzata
     */
    @OptIn( ExperimentalTestApi::class)
    @Test
    fun testVisualizzazione() = runComposeUiTest{
        val p1: Persona = Persona( 1, "Fede", "", 5, 12 )
        val p2:Persona = Persona( 2, "Gatto","", 1, 2 )
        val pList:MutableList<Persona> = mutableListOf( p1, p2 )

        val getDatiMock = mockk<RichiestaDatiService> {
            coEvery { fetchPersonas() } returns pList
        }
        setContent {

            val scope: CoroutineScope = rememberCoroutineScope()
            TabellaTransazione( scope, false, richiesteDati = getDatiMock, onCloseModal = { } )
        }

        //controllo che l'header della tabella siano visualizzati
        onNodeWithTag("headerID" ).assertExists()
        onNodeWithTag( "headerNome").assertExists()
        onNodeWithTag("headerPartecipa").assertExists()
        onNodeWithTag("headerPaga").assertDoesNotExist()
        //ID
        onNodeWithTag("1").assertExists()
        onNodeWithTag("2").assertExists()
        onNodeWithTag("3").assertDoesNotExist()
        //Nome
        onNodeWithTag("Fede").assertExists()
        onNodeWithTag("Gatto").assertExists()
        onNodeWithTag("Geltrude").assertDoesNotExist()
    }

    /**
     * Testo un interazione di invio senza nessun elemento della tabella spuntato
     */
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testInterazioneErrore() = runComposeUiTest{

        val p1: Persona = Persona( 1, "Fede", "", 5, 12 )
        val p2:Persona = Persona( 2, "Gatto","", 1, 2 )
        val pList:MutableList<Persona> = mutableListOf( p1, p2 )

        val getDatiMock = mockk<RichiestaDatiService> {
            coEvery { fetchPersonas() } returns pList
        }
        var closeModal: String = ""
        setContent {
            val scope: CoroutineScope = rememberCoroutineScope()
            TabellaTransazione( scope, false, richiesteDati = getDatiMock, onCloseModal = { closeModal = "cambioContesto" } )
        }

        //verifico che venga visualizzata la finestra di dialogo e che alla sua chiusura venga triggerato l'evento giusto
        onNodeWithTag( "modalDialogCommon" ).assertDoesNotExist()
        onNodeWithTag( "inviaButton" ).performClick()
        onNodeWithTag( "modalDialogCommon" ).assertExists()
        onNodeWithTag( "okButtonModal" ).performClick()
        assertEquals("cambioContesto", closeModal)
    }

    /**
     * Testo un interazione di invio con mock di InvioDati
     */
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testInterazioneSend() = runComposeUiTest{
        //mock

        val p1:Persona = Persona( 1, "Fede", "", 5, 12 )
        val p2:Persona = Persona( 2, "Gatto","", 1, 2 )
        val pList:MutableList<Persona> = mutableListOf( p1, p2 )

        val invioDatiMock = mockk<InvioDatiService> {
            coEvery { sendTransazione(any()) } returns Transazione( id = 1, data = "oggi", partecipanti = pList, pagata_da = p1 )
        }


        val getDatiMock = mockk<RichiestaDatiService> {
            coEvery { fetchPersonas() } returns pList
        }
        var closeModal: String = ""
        setContent {
            val scope: CoroutineScope = rememberCoroutineScope()
            TabellaTransazione( scope, false, richiesteDati = getDatiMock, invioDati = invioDatiMock, onCloseModal = { closeModal = "cambioContesto" } )
        }


        //spunto i parteipanti
        onNodeWithTag( "check1" ).assertExists()
        onNodeWithTag( "check2" ).assertExists()
        onNodeWithTag( "check1" ).performClick()
        onNodeWithTag( "check2" ).performClick()
        //click su invio
        onNodeWithTag( "inviaButton" ).performClick()
        //verifico che il result sia quanto mi aspetto dal mock
        onNodeWithTag( "modalDialogCommon" ).assertExists()
        //onNodeWithText("Riepilogo transazione: \nID:1 \nPartecipanti: \n 1)Fede\n 2)Gatto \nPagata da: Fede").assertExists()
        onNodeWithTag( "okButtonModal" ).performClick()
        assertEquals("cambioContesto", closeModal)
    }

    /**
     * Testo un interazione di invio con mock di InvioDati ed errore di invio
     */
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testInterazioneSendError() = runComposeUiTest{
        //mock

        val p1:Persona = Persona( 1, "Fede", "", 5, 12 )
        val p2:Persona = Persona( 2, "Gatto","", 1, 2 )
        val pList:MutableList<Persona> = mutableListOf( p1, p2 )

        val invioDatiMock = mockk<InvioDatiService> {
            coEvery { sendTransazione(any()) } throws RuntimeException("qualcosa è andato storto")
        }


        val getDatiMock = mockk<RichiestaDatiService> {
            coEvery { fetchPersonas() } returns pList
        }
        var closeModal: String = ""
        setContent {
            val scope: CoroutineScope = rememberCoroutineScope()
            TabellaTransazione( scope, false, richiesteDati = getDatiMock, invioDati = invioDatiMock, onCloseModal = { closeModal = "cambioContesto" } )
        }


        //spunto i parteipanti
        onNodeWithTag( "check1" ).assertExists()
        onNodeWithTag( "check2" ).assertExists()
        onNodeWithTag( "check1" ).performClick()
        onNodeWithTag( "check2" ).performClick()
        onNodeWithTag( "modalDialogCommon" ).assertDoesNotExist()
        //click su invio
        onNodeWithTag( "inviaButton" ).performClick()
        //verifico che il result sia quanto mi aspetto dal mock
        onNodeWithTag( "modalDialogCommon" ).assertExists()
        onNodeWithText("Errore nell'invio dei dati: \nqualcosa è andato storto").assertExists()
        onNodeWithTag( "okButtonModal" ).performClick()
        assertEquals("cambioContesto", closeModal)
    }
}
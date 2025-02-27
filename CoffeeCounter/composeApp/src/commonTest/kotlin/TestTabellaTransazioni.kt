import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import it.insiel.coffeecounter.RichiesteServer.Persona
import it.insiel.coffeecounter.Transazioni.TabellaTransazione
import it.insiel.coffeecounter.Transazioni.TransazioniUI
import kotlinx.coroutines.CoroutineScope
import kotlin.test.Test
import kotlin.test.assertEquals

//import androidx.compose.ui.test.junit4.createComposeRule


class TestTabellaTransazioni {


    /**
     * Testo che la tabella venga visualizzata
     */
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testVisualizzazione() = runComposeUiTest{
        setContent {
            val p1:Persona = Persona( 1, "Fede", "", 5, 12 )
            val p2:Persona = Persona( 2, "Gatto","", 1, 2 )
            val pList:List<Persona> = listOf( p1, p2 )
            val mockTransazioniUI : TransazioniUI = TransazioniUI( pList )
            val scope: CoroutineScope = rememberCoroutineScope()
            TabellaTransazione( mockTransazioniUI, scope, false, onCloseModal = { }, onMyEvent = {} )
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
        var closeModal: String = ""
        setContent {
            val p1:Persona = Persona( 1, "Fede", "", 5, 12 )
            val p2:Persona = Persona( 2, "Gatto","", 1, 2 )
            val pList:List<Persona> = listOf( p1, p2 )
            val mockTransazioniUI : TransazioniUI = TransazioniUI( pList )
            val scope: CoroutineScope = rememberCoroutineScope()
            TabellaTransazione( mockTransazioniUI, scope, false, onCloseModal = { closeModal = "cambioContesto" }, onMyEvent = {} )
        }

        //verifico che venga visualizzata la finestra di dialogo e che alla sua chiusura venga triggerato l'evento giusto
        onNodeWithTag( "modalDialogCommon" ).assertDoesNotExist()
        onNodeWithTag( "inviaButton" ).performClick()
        onNodeWithTag( "modalDialogCommon" ).assertExists()
        onNodeWithTag( "okButtonModal" ).performClick()
        assertEquals("cambioContesto", closeModal)
    }
}
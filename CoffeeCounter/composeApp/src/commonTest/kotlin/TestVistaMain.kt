import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import it.delbix.coffeecounter.VistaMain
import kotlin.test.Test
import kotlin.test.assertEquals

class TestVistaMain {
    /**
     * Testo che la schermata sia visualizzata correttamente
     */
    @OptIn( ExperimentalTestApi::class)
    @Test
    fun testVisualizzazione() = runComposeUiTest {
        setContent {
            VistaMain( onButtonManualeClick = {}, onButtonAutomaticoClick = {})
        }
        onNodeWithTag("automatico").assertExists()
        onNodeWithTag("manuale").assertExists()
    }

    /**
     * Testo che cliccando il bottone automatico venga attivata la funzione giusta
     */
    @OptIn( ExperimentalTestApi::class)
    @Test
    fun testClickAutomatico() = runComposeUiTest {
        var clickEvent = ""
        setContent {
            VistaMain( onButtonManualeClick = {}, onButtonAutomaticoClick = { clickEvent="cambioVista"})
        }
        onNodeWithTag("automatico").assertExists()
        onNodeWithTag("manuale").assertExists()
        onNodeWithTag("automatico").performClick()

        assertEquals("cambioVista", clickEvent)
    }

    /**
     * Testo che cliccando il bottone manuale venga attivata la funzione giusta
     */
    @OptIn( ExperimentalTestApi::class)
    @Test
    fun testClickManuale() = runComposeUiTest {
        var clickEvent = ""
        setContent {
            VistaMain( onButtonManualeClick = { clickEvent="cambioVista" }, onButtonAutomaticoClick = {})
        }
        onNodeWithTag("automatico").assertExists()
        onNodeWithTag("manuale").assertExists()
        onNodeWithTag("manuale").performClick()

        assertEquals("cambioVista", clickEvent)
    }

}
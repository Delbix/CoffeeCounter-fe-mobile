import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import it.insiel.coffeecounter.GestioneUtenti.VistaAddUser
import kotlin.test.Test

class TestAddUser {
    /**
     * Testo che la schermata sia visualizzata correttamente
     */
    @OptIn( ExperimentalTestApi::class)
    @Test
    fun testVisualizzazione() = runComposeUiTest{
        setContent {
            VistaAddUser {  }
        }

        //verifico che i campi nome e cognome esistano
        onNodeWithTag("nome").assertExists()
        onNodeWithTag("cognome").assertExists()
        //controllo che il campo errore non sia visibile
        onNodeWithTag("error").assertDoesNotExist()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testInterazioneErrore() = runComposeUiTest {
        setContent {
            VistaAddUser {  }
        }

        onNodeWithTag("addUser").assertExists()
        onNodeWithTag("error").assertDoesNotExist()
        onNodeWithTag("addUser").performClick()
        onNodeWithTag("error").assertExists()
    }
}
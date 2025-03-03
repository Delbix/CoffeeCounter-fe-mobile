import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import io.mockk.coEvery
import io.mockk.mockk
import it.insiel.coffeecounter.GestioneUtenti.VistaAddUser
import it.insiel.coffeecounter.RichiesteServer.InvioDatiService
import it.insiel.coffeecounter.RichiesteServer.Persona
import kotlin.test.Test
import kotlin.test.assertEquals

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

    /**
     * Test di invio dati con nome vuoto
     */
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

    /**
     * Test di invio dati con mock
     */
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testInvioDati() = runComposeUiTest {
        val invioDatiMock = mockk<InvioDatiService> {
            coEvery { sendPersona(any()) } returns Persona( id = 1, nome = "Fede", cognome = "", ha_partecipato = 0, ha_pagato = 0 )
        }
        var closeModal:String = ""

        setContent {
            VistaAddUser(invioDatiMock, onCloseModal = { closeModal = "cambioContesto" })
        }

        //inserisco un nome utente
        onNodeWithTag("nome").performTextInput("Fede")
        onNodeWithTag("addUser").assertExists()
        onNodeWithTag("error").assertDoesNotExist()
        onNodeWithTag( "modalDialogCommon" ).assertDoesNotExist()
        //clicco su Aggiungi
        onNodeWithTag("addUser").performClick()
        onNodeWithTag("error").assertDoesNotExist()
        //mi aspetto che ci sia la modal aperta
        onNodeWithTag( "modalDialogCommon" ).assertExists()
        onNodeWithTag( "okButtonModal" ).performClick()
        //verifico che la modal si chiuda correttamente
        assertEquals("cambioContesto", closeModal)
    }
}
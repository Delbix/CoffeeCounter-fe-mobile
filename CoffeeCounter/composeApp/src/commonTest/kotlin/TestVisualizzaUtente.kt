import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import io.mockk.coEvery
import io.mockk.mockk
import it.insiel.coffeecounter.GestioneUtenti.VistaAddUser
import it.insiel.coffeecounter.GestioneUtenti.VisualizzaUtente
import it.insiel.coffeecounter.RichiesteServer.InvioDatiService
import it.insiel.coffeecounter.RichiesteServer.Persona
import kotlin.test.Test
import kotlin.test.assertEquals

class TestVisualizzaUtente {

    /**
     * Testo che la schermata sia visualizzata correttamente
     */
    @OptIn( ExperimentalTestApi::class)
    @Test
    fun testVisualizzazione() = runComposeUiTest{
        val p1: Persona = Persona( 1, "Fede", "", 5, 12 )
        setContent {
            VisualizzaUtente(p1) {  }
        }

        //verifico che i campi nome e cognome esistano
        onNodeWithTag("nome").assertExists()
        onNodeWithTag("cognome").assertExists()
        onNodeWithText("Fede").assertExists()
    }

    /**
     * Testo il fatto che se non modifico nulla, mi appaia a video l'errore in rosso
     */
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testInterazioneErrore() = runComposeUiTest {
        val p1: Persona = Persona( 1, "Fede", "", 5, 12 )
        setContent {
            VisualizzaUtente(p1) {  }
        }

        onNodeWithTag("modifica").assertExists()
        onNodeWithTag("error").assertDoesNotExist()
        onNodeWithTag("modifica").performClick()
        onNodeWithTag("error").assertExists()
    }

    /**
     * Testo che modifciando l'utente e mockkando la risposta del server mi funzioni correttamente
     */
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testInterazioneModifica() = runComposeUiTest {
        val invioDatiMock = mockk<InvioDatiService> {
            coEvery { sendPersona(any()) } returns Persona( id = 1, nome = "Fede", cognome = "", ha_partecipato = 0, ha_pagato = 0 )
        }
        var closeModal:String = ""
        val p1: Persona = Persona( 1, "Fede", "", 5, 12 )
        setContent {
            VisualizzaUtente(persona = p1, invioDati = invioDatiMock, onCloseModal = { closeModal = "cambioContesto"})
        }

        //Modifico il nome
        onNodeWithTag("nome").performTextInput("Pippo")
        onNodeWithTag("modifica").assertExists()
        onNodeWithTag("error").assertDoesNotExist()
        onNodeWithTag( "modalDialogCommon" ).assertDoesNotExist()
        //clicco su modifica
        onNodeWithTag("modifica").performClick()
        onNodeWithTag("modalDialogCommon").assertExists()
        onNodeWithTag("error").assertDoesNotExist()
        onNodeWithTag( "modalDialogCommon" ).assertExists()
        //clicco per chiudere la modal
        onNodeWithTag( "okButtonModal" ).performClick()
        //verifico che la modal si chiuda correttamente
        assertEquals("cambioContesto", closeModal)

    }

    /**
     * Testo che cliccando su elimina, mi si apra la procedura di eliminazione
     */
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testInterazioneElimina() = runComposeUiTest {
        val invioDatiMock = mockk<InvioDatiService> {
            coEvery { eliminaPersona(any()) } returns "true"
        }
        var closeModal:String = ""
        val p1: Persona = Persona( 1, "Fede", "", 5, 12 )
        setContent {
            VisualizzaUtente(persona = p1, invioDati = invioDatiMock, onCloseModal = { closeModal = "cambioContesto"})
        }

        //Modifico il nome
        onNodeWithTag("nome").performTextInput("Pippo")
        onNodeWithTag("elimina").assertExists()
        onNodeWithTag("error").assertDoesNotExist()
        onNodeWithTag( "modalDialogCommon" ).assertDoesNotExist()
        //clicco su modifica
        onNodeWithTag("elimina").performClick()
        onNodeWithTag("error").assertDoesNotExist()
        onNodeWithTag( "modalDialogConfirm" ).assertExists()
        //clicco per chiudere la modal
        onNodeWithTag( "conferma" ).performClick()
        onNodeWithTag("modalDialogCommon").assertExists()
        onNodeWithTag( "okButtonModal" ).performClick()
        //verifico che la modal si chiuda correttamente
        assertEquals("cambioContesto", closeModal)

    }
}
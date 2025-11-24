import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import io.mockk.coEvery
import io.mockk.mockk
import it.delbix.coffeecounter.GestioneUtenti.VisualizzaUtente
import it.delbix.coffeecounter.GlobalContext
import it.delbix.coffeecounter.RichiesteServer.InvioDatiService
import it.delbix.coffeecounter.RichiesteServer.Persona
import org.koin.compose.viewmodel.koinViewModel
import kotlin.test.Test
import kotlin.test.assertEquals

class TestVisualizzaUtente {

    /**
     * Testo che la schermata sia visualizzata correttamente
     */
    @OptIn( ExperimentalTestApi::class)
    @Test
    fun testVisualizzazione() = runComposeUiTest{

        setContent {
            val globalContext: GlobalContext = koinViewModel<GlobalContext>()
            globalContext.currentPersonaDaModificare.value.id = 1
            globalContext.currentPersonaDaModificare.value.nome = "Fede"
            globalContext.currentPersonaDaModificare.value.cognome = ""
            globalContext.currentPersonaDaModificare.value.ha_pagato = 5
            globalContext.currentPersonaDaModificare.value.ha_partecipato = 12

            VisualizzaUtente() {  }
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
        setContent {
            val globalContext: GlobalContext = koinViewModel<GlobalContext>()
            globalContext.currentPersonaDaModificare.value.id = 1
            globalContext.currentPersonaDaModificare.value.nome = "Fede"
            globalContext.currentPersonaDaModificare.value.cognome = ""
            globalContext.currentPersonaDaModificare.value.ha_pagato = 5
            globalContext.currentPersonaDaModificare.value.ha_partecipato = 12

            VisualizzaUtente() {  }
        }

        onNodeWithTag("modifica").assertExists()
        onNodeWithTag("error").assertDoesNotExist()
        onNodeWithTag("modifica").performClick()
        onNodeWithTag("error").assertExists()
    }

    /**
     * Testo che modificando l'utente e mockkando la risposta del server mi funzioni correttamente
     */
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testInterazioneModifica() = runComposeUiTest {
        val invioDatiMock = mockk<InvioDatiService> {
            coEvery { sendPersona(any()) } returns Persona( id = 1, nome = "Fede", cognome = "", ha_partecipato = 0, ha_pagato = 0, caffe_pagati = 0 )
        }
        var closeModal:String = ""
        setContent {
            val globalContext: GlobalContext = koinViewModel<GlobalContext>()
            globalContext.currentPersonaDaModificare.value.id = 1
            globalContext.currentPersonaDaModificare.value.nome = "Fede"
            globalContext.currentPersonaDaModificare.value.cognome = ""
            globalContext.currentPersonaDaModificare.value.ha_pagato = 5
            globalContext.currentPersonaDaModificare.value.ha_partecipato = 12

            VisualizzaUtente(invioDati = invioDatiMock, onCloseModal = { closeModal = "cambioContesto"})
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
     * Testo che modificando l'utente e mockkando la risposta del server(error response) mi funzioni correttamente
     */
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testInterazioneModificaErrore() = runComposeUiTest {
        val invioDatiMock = mockk<InvioDatiService> {
            coEvery { sendPersona(any()) } throws RuntimeException("c'è stato un problema")
        }
        var closeModal:String = ""
        setContent {
            val globalContext: GlobalContext = koinViewModel<GlobalContext>()
            globalContext.currentPersonaDaModificare.value.id = 1
            globalContext.currentPersonaDaModificare.value.nome = "Fede"
            globalContext.currentPersonaDaModificare.value.cognome = ""
            globalContext.currentPersonaDaModificare.value.ha_pagato = 5
            globalContext.currentPersonaDaModificare.value.ha_partecipato = 12

            VisualizzaUtente(invioDati = invioDatiMock, onCloseModal = { closeModal = "cambioContesto"})
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
        onNodeWithText("c'è stato un problema").assertExists()
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
        setContent {
            val globalContext: GlobalContext = koinViewModel<GlobalContext>()
            globalContext.currentPersonaDaModificare.value.id = 1
            globalContext.currentPersonaDaModificare.value.nome = "Fede"
            globalContext.currentPersonaDaModificare.value.cognome = ""
            globalContext.currentPersonaDaModificare.value.ha_pagato = 5
            globalContext.currentPersonaDaModificare.value.ha_partecipato = 12

            VisualizzaUtente(invioDati = invioDatiMock, onCloseModal = { closeModal = "cambioContesto"})
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

    /**
     * Testo che cliccando su elimina, mi si apra la procedura di eliminazione (ma qualcosa va storto)
     */
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testInterazioneEliminaErrore() = runComposeUiTest {
        val invioDatiMock = mockk<InvioDatiService> {
            coEvery { eliminaPersona(any()) } returns "false" //qualcosa è andato storto
        }
        var closeModal:String = ""
        setContent {
            val globalContext: GlobalContext = koinViewModel<GlobalContext>()
            globalContext.currentPersonaDaModificare.value.id = 1
            globalContext.currentPersonaDaModificare.value.nome = "Fede"
            globalContext.currentPersonaDaModificare.value.cognome = ""
            globalContext.currentPersonaDaModificare.value.ha_pagato = 5
            globalContext.currentPersonaDaModificare.value.ha_partecipato = 12

            VisualizzaUtente(invioDati = invioDatiMock, onCloseModal = { closeModal = "cambioContesto"})
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
        onNodeWithText("C'è stato un errore durante l'eliminazione, riprova!").assertExists()
        onNodeWithTag( "okButtonModal" ).performClick()
        //verifico che la modal si chiuda correttamente
        assertEquals("cambioContesto", closeModal)

    }

    /**
     * Testo che cliccando su elimina, mi si apra la procedura di eliminazione (ma il server non risponde)
     */
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun testInterazioneEliminaErrore2() = runComposeUiTest {
        val invioDatiMock = mockk<InvioDatiService> {
            coEvery { eliminaPersona(any()) } throws RuntimeException("c'è stato un problema") //exception
        }
        var closeModal:String = ""
        setContent {
            val globalContext: GlobalContext = koinViewModel<GlobalContext>()
            globalContext.currentPersonaDaModificare.value.id = 1
            globalContext.currentPersonaDaModificare.value.nome = "Fede"
            globalContext.currentPersonaDaModificare.value.cognome = ""
            globalContext.currentPersonaDaModificare.value.ha_pagato = 5
            globalContext.currentPersonaDaModificare.value.ha_partecipato = 12

            VisualizzaUtente(invioDati = invioDatiMock, onCloseModal = { closeModal = "cambioContesto"})
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
        onNodeWithText("c'è stato un problema").assertExists()
        onNodeWithTag( "okButtonModal" ).performClick()
        //verifico che la modal si chiuda correttamente
        assertEquals("cambioContesto", closeModal)

    }
}
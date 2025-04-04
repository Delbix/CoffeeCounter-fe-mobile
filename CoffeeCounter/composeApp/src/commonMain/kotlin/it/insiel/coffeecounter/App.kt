package it.insiel.coffeecounter

import androidx.compose.foundation.clickable
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import it.insiel.coffeecounter.GestioneUtenti.VistaAddUser
import org.jetbrains.compose.ui.tooling.preview.Preview

import it.insiel.coffeecounter.GestioneUtenti.VistaUtenti
import it.insiel.coffeecounter.GestioneUtenti.VisualizzaUtente
import it.insiel.coffeecounter.InformazioniApp.VistaInfo
import it.insiel.coffeecounter.RichiesteServer.Persona
import it.insiel.coffeecounter.Statistiche.vistaStatistiche

import it.insiel.coffeecounter.Transazioni.VistaTransazioniNew
import kotlinx.coroutines.launch

@Composable
@Preview

        /**
         * ENTRY POINT dell'app
         * Gestisco:
         * - Le view (currentView)
         * - Il menu laterale a scomparsa (Drawer)
         * - La barra in alto
         */

fun App() {
    //variabile che tiene conto della vista corrente
    var currentView by remember { mutableStateOf("main") }
    //visibilità del menu laterale
    var drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var user: Persona = Persona( -1, "", "", 0,0, false )//per gestire il visualizza singolo utente

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            SideMenu(
                onUserManagementClick = {
                    scope.launch { drawerState.close() }
                    currentView = "gestioneUtenti"
                },
                onStatisticsClick = {
                    scope.launch { drawerState.close() }
                    currentView = "statistiche"
                },
                oninfoClick = {
                    scope.launch { drawerState.close() }
                    currentView = "visualizzaInfo"
                }
            )
        },
        gesturesEnabled = drawerState.isOpen,
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                "Coffee Counter APP",
                                modifier = Modifier.clickable { currentView = "main" }
                            )
                        },
                        actions = {
                            IconButton(onClick = {
                                scope.launch { drawerState.open() }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "sideMenu")
                            }
                        }
                    )
                }
            ){
                when (currentView) {
                    "main" -> VistaMain(onButtonAutomaticoClick = { currentView = "vistaTransazioniA" }, onButtonManualeClick = { currentView = "vistaTransazioniM"})
                    "vistaTransazioniA" -> VistaTransazioniNew( onSuccesfullySend = { currentView = "main" } )
                    "vistaTransazioniM" -> VistaTransazioniNew( manuale = true, onSuccesfullySend = { currentView = "main" } )
                    "gestioneUtenti" -> VistaUtenti(
                        onClickAddUser = { currentView = "aggiungiUtente" },
                        onVisualizzaUtente = {
                            persona -> currentView = "visualizzaUtente"
                            user = persona
                        },
                        onErrorDetected = { currentView = "main" }
                    )
                    "aggiungiUtente" -> VistaAddUser( onCloseModal = { currentView = "gestioneUtenti" } )
                    "statistiche" -> vistaStatistiche( onCloseModal = { currentView = "main" } )
                    "visualizzaUtente" -> VisualizzaUtente( user, onCloseModal = { currentView = "gestioneUtenti"} )
                    "visualizzaInfo" -> VistaInfo()
                }
            }
        }
    )
}
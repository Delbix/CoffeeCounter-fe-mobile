package it.delbix.coffeecounter

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import coffeecounter.composeapp.generated.resources.Res
import coffeecounter.composeapp.generated.resources.accAppIcon
import coffeecounter.composeapp.generated.resources.accDrawerIcon
import coffeecounter.composeapp.generated.resources.accInfoIcon
import coffeecounter.composeapp.generated.resources.accSettingsIcon
import coffeecounter.composeapp.generated.resources.app_title
import coffeecounter.composeapp.generated.resources.gestioneUtenti
import coffeecounter.composeapp.generated.resources.java
import coffeecounter.composeapp.generated.resources.statistiche
import it.delbix.coffeecounter.GestioneUtenti.VistaAddUser
import it.delbix.coffeecounter.GestioneUtenti.VistaUtenti
import it.delbix.coffeecounter.GestioneUtenti.VisualizzaUtente
import it.delbix.coffeecounter.InformazioniApp.VistaInfo
import it.delbix.coffeecounter.RichiesteServer.Persona
import it.delbix.coffeecounter.Settings.VistaSettings
import it.delbix.coffeecounter.Statistiche.vistaStatistiche
import it.delbix.coffeecounter.Transazioni.VistaTransazioniNew
import it.delbix.coffeecounter.di.composeModule
import it.delbix.coffeecounter.ui.theme.AppTheme
import it.delbix.coffeecounter.utils.Logger
import it.delbix.coffeecounter.utils.ScreenUtils
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.context.startKoin

@OptIn(ExperimentalMaterial3Api::class)
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
    startKoin{
        modules(composeModule)
    }
    AppTheme {
        val globalContext: GlobalContext = koinViewModel<GlobalContext>()
        //variabile che tiene conto della vista corrente
        val currentView = remember { mutableStateOf("main") }
        //visibilità del menu laterale
        var drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        //larghezza fissata del drawer al 70% del monitor
            //TODO non sembra funzionare in alcun modo sta cosa
        val drawerWidth = ScreenUtils.getScreenWidth() *0.7

        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = drawerState.isOpen,
    //        modifier = Modifier
    //            .width(drawerWidth.dp),
            drawerContent = {
                ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.secondaryContainer) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(16.dp)
                            .width(drawerWidth.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(Res.string.gestioneUtenti),
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable(onClick = {
                                    scope.launch { drawerState.close() }
                                    currentView.value = "gestioneUtenti"
                                })
                                .testTag("gestioneUtentiNode")
                        )
                        Text(
                            text = stringResource(Res.string.statistiche),
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable(onClick = {
                                    scope.launch { drawerState.close() }
                                    currentView.value = "statistiche"
                                })
                                .testTag("statisticheNode")
                        )
                        Icon(
                            Icons.Default.Info,
                            contentDescription = stringResource(Res.string.accInfoIcon),
                            modifier = Modifier
                                .clickable(onClick = {
                                    scope.launch { drawerState.close() }
                                    currentView.value = "visualizzaInfo"
                                })
                                .padding(16.dp)
                                .testTag("infoNode")
                        )
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = stringResource(Res.string.accSettingsIcon),
                            modifier = Modifier
                                .clickable(onClick = {
                                    scope.launch { drawerState.close() }
                                    currentView.value = "visualizzaSettings"
                                })
                                .padding(16.dp)
                        )
                    }
                }
            },
            content = {
                Scaffold(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    topBar = {
                        TopAppBar(
                            title = {
                                Row(
                                    modifier = Modifier.clickable { currentView.value = "main" },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    val textMeasurer = rememberTextMeasurer()
                                    val textHeight = textMeasurer.measure(stringResource(Res.string.app_title)).size.height

                                    Image(
                                        painter = painterResource(Res.drawable.java),
                                        contentDescription = stringResource(Res.string.accAppIcon),
                                        modifier = Modifier.size((textHeight-20).dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        stringResource( Res.string.app_title ),
                                        modifier = Modifier.alignBy(FirstBaseline)
                                    )
                                }
                            },

                            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                            actions = {
                                IconButton(
                                    modifier = Modifier.testTag("drawerButton"),
                                    onClick = {
                                        scope.launch { drawerState.open() }
                                    }) {
                                    Icon(Icons.Default.Menu, contentDescription = stringResource(Res.string.accDrawerIcon))
                                }
                            }
                        )
                    }
                ) {
                    paddingValues -> //questo serve ad avere un valore di padding già calcolato, basta solo passarlo alle altre viste
                    var user: Persona =
                        Persona(-1, "", "", 0, 0, 0, false)//per gestire il visualizza singolo utente

                    when (currentView.value) {
                        "main" -> VistaMain(
                            onButtonAutomaticoClick = {
                                currentView.value = "vistaTransazioniA"
                                                      Logger.log("automatico")
                                                      },
                            onButtonManualeClick = { currentView.value = "vistaTransazioniM" }
                        )

                        "vistaTransazioniA" -> VistaTransazioniNew(onSuccesfullySend = {
                            currentView.value = "main"
                        })

                        "vistaTransazioniM" -> VistaTransazioniNew(
                            manuale = true,
                            onSuccesfullySend = { currentView.value = "main" })

                        "gestioneUtenti" -> VistaUtenti(
                            paddingValues = paddingValues,
                            onClickAddUser = { currentView.value = "aggiungiUtente" },
                            onVisualizzaUtente = { persona ->
                                currentView.value = "visualizzaUtente"
                                user = persona
                            },
                            onErrorDetected = { currentView.value = "main" }
                        )

                        "aggiungiUtente" -> VistaAddUser(onCloseModal = {
                            currentView.value = "gestioneUtenti"
                        })

                        "statistiche" -> vistaStatistiche(onCloseModal = { currentView.value = "main" })

                        "visualizzaUtente" -> VisualizzaUtente(
                            onCloseModal = { currentView.value = "gestioneUtenti" })

                        "visualizzaInfo" -> VistaInfo()

                        "visualizzaSettings" -> VistaSettings(paddingValues = paddingValues)
                    }
                }
            })
        }
    }
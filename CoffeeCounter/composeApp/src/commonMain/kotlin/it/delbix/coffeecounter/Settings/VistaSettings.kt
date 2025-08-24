package it.delbix.coffeecounter.Settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coffeecounter.composeapp.generated.resources.Res
import coffeecounter.composeapp.generated.resources.criterio1
import coffeecounter.composeapp.generated.resources.criterio2
import coffeecounter.composeapp.generated.resources.criterioSettingDescription
import org.jetbrains.compose.resources.stringResource

/**
 * VISTA
 * Gestione delle inpostazioni dell'app
 *
 * **Parametri**
 * @param paddingValues = valore per non far finire i testi sotto alla top bar
 * **Lambda**
 *
 */

@Composable
fun VistaSettings( paddingValues: PaddingValues ){
    val selectedCriterio = remember { mutableStateOf( CriterioAutomaticoManager.recuperaCriterioAutomatico() ) }

    Column(modifier = Modifier.padding(paddingValues)) {
        Text(
            stringResource(Res.string.criterioSettingDescription),
            style = MaterialTheme.typography.titleMedium)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = selectedCriterio.value == 1,
                onCheckedChange = {
                    selectedCriterio.value = 1
                    CriterioAutomaticoManager.salvaCriterioAutomatico(1)
                }
            )
            Text(stringResource(Res.string.criterio1))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = selectedCriterio.value == 2,
                onCheckedChange = {
                    selectedCriterio.value = 2
                    CriterioAutomaticoManager.salvaCriterioAutomatico(2)
                }
            )
            Text(stringResource(Res.string.criterio2))
        }
    }
}
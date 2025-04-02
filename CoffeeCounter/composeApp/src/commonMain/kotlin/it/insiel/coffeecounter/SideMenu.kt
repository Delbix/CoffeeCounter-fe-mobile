package it.insiel.coffeecounter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * VISTA
 * del menu a scomparsa laterale
 *
 * **Parametri**
 *
 * **Lambda**
 * @param onUserManagementClick = viene gestito da App.kt per cambiare la vista
 * @param onStatisticsClick = viene gestito da App.kt per cambiare la vista
 */

@Composable
fun SideMenu(
    onUserManagementClick: () -> Unit,
    onStatisticsClick: () -> Unit,
    oninfoClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Icon(
            Icons.Default.Info,
            contentDescription = "sideMenu",
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = oninfoClick)
        )
        Text(
            text = "Gestione utenti",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable(onClick = onUserManagementClick)
        )
        Text(
            text = "Statistiche",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable(onClick = onStatisticsClick)
        )
    }
}
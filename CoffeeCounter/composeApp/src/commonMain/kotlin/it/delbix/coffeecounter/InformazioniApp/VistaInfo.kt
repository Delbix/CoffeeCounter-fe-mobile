package it.delbix.coffeecounter.InformazioniApp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coffeecounter.composeapp.generated.resources.Res
import coffeecounter.composeapp.generated.resources.appInfoTitle
import coffeecounter.composeapp.generated.resources.currentVersion
import coffeecounter.composeapp.generated.resources.infoDevelopers
import it.delbix.coffeecounter.AppInit
import org.jetbrains.compose.resources.stringResource

/**
 * VISTA
 * Informazioni generiche sull'app
 */

@Composable
fun VistaInfo(){ //globalContext: GlobalContext = get()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(Res.string.appInfoTitle), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp)
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(Res.string.currentVersion, AppInit.VERSION), modifier = Modifier.testTag("currentVersion"))
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp)
        ) {
            Icon(Icons.Default.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(Res.string.infoDevelopers))
        }
    }
}
package it.insiel.coffeecounter.InformazioniApp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.insiel.coffeecounter.AppInit
import it.insiel.coffeecounter.RichiesteServer.Persona
import it.insiel.coffeecounter.Statistiche.bevitoreAccanito
import it.insiel.coffeecounter.Statistiche.pagatore

@Composable
fun VistaInfo(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Informazioni sull'app", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp)
        ) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Current version "+AppInit.VERSION)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp)
        ) {
            Icon(Icons.Default.Check, contentDescription = "sideMenu")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Sviluppata da Federico su supervisione di Christian")
        }
    }
}
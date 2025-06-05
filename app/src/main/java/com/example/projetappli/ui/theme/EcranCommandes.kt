package com.example.projetappli.ui.theme

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projetappli.repository.JeuViewModel

@Composable
fun EcranCommandes(jeuViewModel: JeuViewModel = viewModel()) {
    val scrollState = rememberScrollState()
    val commandesDebloquees by jeuViewModel.commandesDisponibles.collectAsState()

    val toutesLesCommandes = listOf(
        "./start" to "Démarre l'aventure.",
        "./createCharacter -n [Nom]" to "Crée un personnage dénommé [Nom]",
        "./switchCharacter -n [Nom]" to "Permet de changer le personnage sélectionné",
        "./quest -n [N°Quete] -s [Etape]" to "Permet d'acceder à l'étape [Etape] de la quete [N°Quete]",
        "./currentQuest" to "Permet de connaitre la quête en cours"
    )

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        toutesLesCommandes.filter { (cmd, _) -> commandesDebloquees.contains(cmd) }
            .forEach { (cmd, description) ->
                AccordeonCommande(
                    command = cmd,
                    description = description
                )
            }
    }

}

@Composable
fun AccordeonCommande(
    command: String,
    description: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize()
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(VertFonce, RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (expanded) "−" else "+",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = command,
                fontSize = 18.sp,
                fontFamily = orbitFontFamily,
                color = Color.White
            )
        }

        if (expanded) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                fontSize = 16.sp,
                fontFamily = orbitFontFamily,
                color = Color.White,
                modifier = Modifier.padding(start = 52.dp)
            )
        }
    }
}



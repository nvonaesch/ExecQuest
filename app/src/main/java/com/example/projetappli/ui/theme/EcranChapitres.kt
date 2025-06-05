package com.example.projetappli.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projetappli.repository.JeuViewModel

@Composable
fun EcranChapitres(jeuViewModel: JeuViewModel = viewModel()) {
    val scrollState = rememberScrollState()
    val chapitres by jeuViewModel.chapitresDisponibles.collectAsState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        chapitres.forEach { chapitre ->
            if (chapitre.estDebloque) {
                AccordeonCommande(
                    command = chapitre.titre,
                    description = chapitre.contenu
                )
            }
        }
    }
}

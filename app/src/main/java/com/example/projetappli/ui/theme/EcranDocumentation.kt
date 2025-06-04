package com.example.projetappli.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projetappli.repository.JeuViewModel


@Composable
fun EcranDocumentation(jeuViewModel: JeuViewModel = viewModel()) {
    var selectedTab by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CouleurMain)
    ) {
        NavBarDocumentation(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )

        when (selectedTab) {
            0 -> EcranCommandes(jeuViewModel)
            1 -> EcranChapitres(jeuViewModel)
        }


    }


}

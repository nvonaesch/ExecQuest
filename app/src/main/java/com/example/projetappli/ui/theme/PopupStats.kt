package com.example.projetappli.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetappli.R

import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.example.projetappli.dataclasses.Personnage
import com.example.projetappli.repository.JeuViewModel

@Composable
fun PopupStats(personnage: Personnage, viewModel: JeuViewModel = viewModel(), onClose: () -> Unit) {
    val personnageCourant by viewModel.personnageActif.collectAsState()

    personnageCourant?.let { personnage ->
        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0x88000000))
                .clickable(onClick = onClose)
        ) {
            Box(
                Modifier
                    .align(Alignment.Center)
                    .padding(24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF2B2B2B))
                    .border(4.dp, Color(0xFFB0E000), RoundedCornerShape(12.dp))
                    .padding(16.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {}
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(200.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.coupry),
                        contentDescription = "Portrait ${personnage.nom}",
                        modifier = Modifier
                            .size(64.dp)
                            .padding(bottom = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("NAME: ${personnage.nom}", fontFamily = orbitFontFamily, color = Color.White)
                    Text("HP: ${personnage.hp}", fontFamily = orbitFontFamily, color = Color.White)
                    Text("STR: ${personnage.str}", fontFamily = orbitFontFamily, color = Color.White)
                    Text("INT: ${personnage.int}", fontFamily = orbitFontFamily, color = Color.White)
                    Text("DEF: ${personnage.def}", fontFamily = orbitFontFamily, color = Color.White)
                    Text("SPD: ${personnage.spd}", fontFamily = orbitFontFamily, color = Color.White)

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        Modifier
                            .align(Alignment.End)
                            .clickable { onClose() }
                    ) {
                        Text("✕", fontSize = 20.sp, color = Color.Red)
                    }
                }
            }
        }
    }
}

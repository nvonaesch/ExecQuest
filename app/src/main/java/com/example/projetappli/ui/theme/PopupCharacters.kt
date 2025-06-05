package com.example.projetappli.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetappli.R
import com.example.projetappli.dataclasses.Personnage

@Composable
fun PopupCharacters(
    personnages: List<Personnage>,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xAA000000))
            .padding(32.dp)
            .clickable(onClick = onClose),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color(0xFFD6E9A3), RoundedCornerShape(24.dp))
                .border(6.dp, Color(0xFF2F2F2F), RoundedCornerShape(24.dp))
                .padding(16.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {} 
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                personnages.forEach { perso ->
                    CharacterCard(perso)
                }
            }
        }
    }
}

@Composable
fun CharacterCard(personnage: Personnage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFA8C84D), RoundedCornerShape(16.dp))
            .border(2.dp, Color.DarkGray, RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.coupry), // ou dynamique si tu veux
            contentDescription = personnage.nom,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = personnage.nom,
                fontFamily = orbitFontFamily,
                fontSize = 18.sp,
                color = Color(0xFF2F2F2F)
            )
            Text(
                text = "Level ${personnage.niveau ?: 1}",
                fontFamily = orbitFontFamily,
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }
    }
}

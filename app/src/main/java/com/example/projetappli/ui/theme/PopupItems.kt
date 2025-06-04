package com.example.projetappli.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.projetappli.R
import com.example.projetappli.dataclasses.Item

@Composable
fun PopupItems(
    items: List<Item>,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xAA000000))
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color(0xFFD6E9A3), RoundedCornerShape(24.dp))
                .border(6.dp, Color(0xFF2F2F2F), RoundedCornerShape(24.dp))
                .padding(16.dp)
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)) {

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items.forEach { item ->
                        ItemCard(
                            name = item.name,
                            imageUrl = item.imageUrl,
                            bonusHp = item.bonus_hp,
                            bonusStr = item.bonus_str,
                            bonusInt = item.bonus_int,
                            bonusDef = item.bonus_def,
                            bonusSpd = item.bonus_spd,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ItemCard(
    name: String,
    imageUrl: String,
    bonusHp: Int,
    bonusStr: Int,
    bonusInt: Int,
    bonusDef: Int,
    bonusSpd: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(Color(0xFFA8C84D), RoundedCornerShape(16.dp))
            .border(2.dp, Color.DarkGray, RoundedCornerShape(16.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = name,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp)),
            placeholder = painterResource(R.drawable.icone_doc),
            error = painterResource(R.drawable.fireball_9)
        )

        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = name,
                fontSize = 18.sp,
                fontFamily = orbitFontFamily,
                color = Color(0xFF2F2F2F)
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (bonusHp != 0) {
                StatRow("${formatSigned(bonusHp)} hp", if (bonusHp >= 0) CouleurMain else Color.Red)
            }
            if (bonusStr != 0) {
                StatRow("${formatSigned(bonusStr)} str", if (bonusStr >= 0) CouleurMain else Color.Red)
            }
            if (bonusInt != 0) {
                StatRow("${formatSigned(bonusInt)} int", if (bonusInt >= 0) CouleurMain else Color.Red)
            }
            if (bonusDef != 0) {
                StatRow("${formatSigned(bonusDef)} def", if (bonusDef >= 0) CouleurMain else Color.Red)
            }
            if (bonusSpd != 0) {
                StatRow("${formatSigned(bonusSpd)} spd", if (bonusSpd >= 0) CouleurMain else Color.Red)
            }
        }
    }
}

@Composable
fun StatRow(text: String, color: Color) {
    Text(
        text = text,
        color = color,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = orbitFontFamily
    )
}

fun formatSigned(value: Int): String {
    return if (value >= 0) "+$value" else "$value"
}

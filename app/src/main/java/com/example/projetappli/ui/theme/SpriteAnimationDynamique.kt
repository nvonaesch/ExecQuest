package com.example.projetappli.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.projetappli.R
import kotlinx.coroutines.delay

@Composable
fun SpriteAnimationDynamique(command: String) {
    val frames = when (command) {
        "fireball" -> listOf(
            painterResource(id = R.drawable.fireball_1),
            painterResource(id = R.drawable.fireball_2),
            painterResource(id = R.drawable.fireball_3),
            painterResource(id = R.drawable.fireball_4),
            painterResource(id = R.drawable.fireball_5),
            painterResource(id = R.drawable.fireball_6),
            painterResource(id = R.drawable.fireball_7),
            painterResource(id = R.drawable.fireball_8),
            painterResource(id = R.drawable.fireball_9),
            painterResource(id = R.drawable.fireball_10),
            painterResource(id = R.drawable.fireball_11),
            painterResource(id = R.drawable.fireball_12),
            painterResource(id = R.drawable.fireball_13),
            painterResource(id = R.drawable.fireball_14)
        )
        "apparicio" -> listOf(
            painterResource(id = R.drawable.apparicio)
        )
        "idle" -> listOf(
            painterResource(id =  R.drawable.idle)
        )
        "florine" -> listOf(
            painterResource(id = R.drawable.florine)
        )
        "coupry" -> listOf(
            painterResource(id = R.drawable.coupry)
        )
        else -> listOf(
            painterResource(id = R.drawable.transparent)
        )
    }

    var frame by remember { mutableStateOf(0) }

    LaunchedEffect(command) {
        while (true) {
            delay(200)
            frame = (frame + 1) % frames.size
        }
    }

    Image(
        painter = frames[frame],
        contentDescription = null,
        modifier = Modifier.size(100.dp)
    )
}

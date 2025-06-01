package com.example.projetappli.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetappli.R
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.delay

val orbitFontFamily = FontFamily(Font(R.font.orbit_regular))
@Composable
fun EcranCombat() {
    var input by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf("Message de test de bouffon")) }

    var spriteGauche by remember { mutableStateOf<String?>(null) }
    var spriteCentre by remember { mutableStateOf<String?>(null) }
    var spriteDroite by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameScreenFrame(
            gaucheCommand = spriteGauche,
            centreCommand = spriteCentre,
            droiteCommand = spriteDroite
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                BoutonVert("STATS") {}
                BoutonVert("RUNES") {}
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                BoutonVert("CHARACTERS") {}
                BoutonVert("ITEMS") {}
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        BlocDialogueAvecInput(
            messages = messages,
            texteInput = input,
            onTexteChange = { input = it },
            onEnvoyer = {
                if (input.isNotBlank()) {
                    messages = messages + "→ $input"

                    when (input) {
                        "./fireball" -> {
                            spriteCentre = "fireball"
                            spriteDroite = "fireball"
                        }

                        "./gauche" -> spriteGauche = "idle"
                        "./droite" -> spriteDroite = "idle"
                        "./reset" -> {
                            spriteGauche = null
                            spriteCentre = null
                            spriteDroite = null
                        }
                        else -> {

                        }
                    }

                    input = ""
                }
            }
        )
    }
}


@Composable
fun BoutonVert(texte: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = VertPastelle,
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .width(140.dp)
            .height(48.dp)
    ) {
        Text(texte, fontFamily = orbitFontFamily,fontWeight = FontWeight.Bold)
    }
}

@Composable
fun GameScreenFrame(
    gaucheCommand: String?,
    centreCommand: String?,
    droiteCommand: String?
) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .height(250.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF7A7A7A)),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (gaucheCommand != null) {
                        SpriteAnimationDynamique(command = gaucheCommand)
                    }
                    if (centreCommand != null) {
                        SpriteAnimationDynamique(command = centreCommand)
                    }
                    if (droiteCommand != null) {
                        SpriteAnimationDynamique(command = droiteCommand)
                    }
                }
            }

            Text(
                text = "ExecQuest",
                color = Color.White,
                fontSize = 24.sp,
                fontFamily = orbitFontFamily,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlocDialogueAvecInput(
    messages: List<String>,
    texteInput: String,
    onTexteChange: (String) -> Unit,
    onEnvoyer: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxSize()
            ) {
                messages.forEach { msg ->
                    Text(
                        text = msg,
                        fontFamily = orbitFontFamily,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = texteInput,
                onValueChange = onTexteChange,
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.LightGray,

                ),
                placeholder = { Text("Écris ta commande...") }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White)
                    .clickable { onEnvoyer() },
                contentAlignment = Alignment.Center
            ) {
                Text("➤", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun EcranAvecDialogue() {
    var input by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf("")) }

    BlocDialogueAvecInput(
        messages = messages,
        texteInput = input,
        onTexteChange = { input = it },
        onEnvoyer = {
            if (input.isNotBlank()) {
                messages = messages + "→ $input"
                input = ""
            }
        }
    )
}

@Composable
fun SpriteAnime() {
    val sprites = listOf(
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
        painterResource(id = R.drawable.fireball_14),

    )
    var frame by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(200)
            frame = (frame + 1) % sprites.size
        }
    }

    Image(
        painter = sprites[frame],
        contentDescription = "Sprite animé",
        modifier = Modifier
            .size(150.dp)
    )
}

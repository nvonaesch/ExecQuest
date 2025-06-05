package com.example.projetappli.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetappli.R
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import com.example.projetappli.dataclasses.Personnage
import com.example.projetappli.repository.JeuViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

val orbitFontFamily = FontFamily(Font(R.font.orbit_regular))

@Composable
fun EcranCombat(jeuViewModel: JeuViewModel = viewModel()) {
    var showStats by remember { mutableStateOf(false) }
    var showItems by remember { mutableStateOf(false) }
    var showCharacters by remember { mutableStateOf(false) }

    val personnageDede = Personnage("DEDE", 50, 12, 2, 8, 12)


    var input by remember { mutableStateOf("") }
    val messages by jeuViewModel.messages.collectAsState()

    val spriteDroite by jeuViewModel.spriteDroite.collectAsState()
    val spriteCentre by jeuViewModel.spriteMilieu.collectAsState()
    val spriteGauche by jeuViewModel.spriteGauche.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LaunchedEffect(Unit) {
            jeuViewModel.chargerItems(listOf(1055, 224403, 3006, 1082, 3047))
        }

        val items by jeuViewModel.items.collectAsState()

        GameScreenFrame(
            gaucheCommand = spriteGauche,
            centreCommand = spriteCentre,
            droiteCommand = spriteDroite
        )

        Spacer(modifier = Modifier.height(10.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                BoutonVert("STATS") {
                    showStats = true
                }
                BoutonVert("RUNES") {}
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                BoutonVert("CHARACTERS") {
                    showCharacters = true
                }
                BoutonVert("ITEMS") {
                    showItems = true

                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        BlocDialogueAvecInput(
            messages = messages,
            texteInput = input,
            onTexteChange = { input = it },
            onEnvoyer = {
                if (input.isNotBlank()) {
                    jeuViewModel.envoyerCommande(input)

                    input = ""
                }
            }
        )
    }
    if (showStats) {
        PopupStats(personnage = personnageDede) {
            showStats = false
        }
    }

    if (showItems) {
        val items = jeuViewModel.items.collectAsState().value
        PopupItems(items = items) {
            showItems = false
        }
    }

    if (showCharacters) {
        val personnages = jeuViewModel.personnages.collectAsState().value
        PopupCharacters(personnages = personnages) {
            showCharacters = false
        }
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
            .width(148.dp)
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
        val scrollState = rememberScrollState()
        LaunchedEffect(messages.size) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
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
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState)
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

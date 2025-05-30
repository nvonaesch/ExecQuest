package com.example.projetappli.ui.theme
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.runtime.Composable
import com.example.projetappli.R

@Composable
fun CustomIconDoc() {
    Image(
        painter = painterResource(id = R.drawable.icone_doc),
        contentDescription = "Documentation",
        modifier = Modifier.size(24.dp),
        contentScale = ContentScale.Fit
    )
}

@Composable
fun CustomIconEpee() {
    Image(
        painter = painterResource(id = R.drawable.icone_epee),
        contentDescription = "Jeu",
        modifier = Modifier.size(24.dp),
        contentScale = ContentScale.Fit
    )
}
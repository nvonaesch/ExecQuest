    package com.example.projetappli
    import android.content.Context
    import android.media.MediaPlayer
    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.activity.enableEdgeToEdge
    import androidx.compose.foundation.background
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.WindowInsets
    import androidx.compose.foundation.layout.asPaddingValues
    import androidx.compose.foundation.layout.fillMaxHeight
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.navigationBars
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.safeDrawing
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material3.*
    import androidx.compose.runtime.*
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.draw.clip
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.platform.LocalContext
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import com.example.projetappli.ui.theme.CouleurMain
    import com.example.projetappli.ui.theme.CustomIconDoc
    import com.example.projetappli.ui.theme.CustomIconEpee
    import com.example.projetappli.ui.theme.EcranCombat
    import com.example.projetappli.ui.theme.EcranDocumentation
    import com.example.projetappli.ui.theme.ProjetAppliTheme
    import com.example.projetappli.ui.theme.VertFonce
    import com.example.projetappli.ui.theme.VertPastelle

    class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContent {
                ProjetAppliTheme {
                    MainScreen()
                }
            }
        }
    }

    @Composable
    fun MainScreen() {
        val context = LocalContext.current
        var selectedIndex by remember { mutableStateOf(0) }
        AmbianceSonore(context, R.raw.redstone128_the_final_battle)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CouleurMain)
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                contentWindowInsets = WindowInsets.safeDrawing,
                containerColor = Color.Transparent,
                bottomBar = {
                    BottomNavigationBar(selectedIndex) { selectedIndex = it }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (selectedIndex) {
                        0 -> EcranCombat()
                        1 -> EcranDocumentation()
                        else -> Greeting("Android")
                    }
                }
            }
        }
    }



    @Composable
    fun BottomNavigationBar(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
        val items = listOf(
            NavBarItem("Combat", { isSelected -> CustomIconEpee() }),
            NavBarItem("Inventaire", { isSelected -> CustomIconDoc() })
        )

        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
                .clip(RoundedCornerShape(20.dp))
                .background(VertFonce)
                .height(60.dp)
                .fillMaxWidth()
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex
                val backgroundColor = if (isSelected) VertPastelle else VertFonce

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(backgroundColor)
                        .clickable { onItemSelected(index) },
                    contentAlignment = Alignment.Center
                ) {
                    item.icon(isSelected)
                }
            }
        }
    }



    data class NavBarItem(
        val label: String,
        val icon: @Composable (Boolean) -> Unit
    )


    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }


    @Composable
    fun AmbianceSonore(context: Context, rawResId: Int) {
        var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

        DisposableEffect(Unit) {
            val player = MediaPlayer.create(context, rawResId)
            player.isLooping = true
            player.start()
            mediaPlayer = player

            onDispose {
                player.stop()
                player.release()
            }
        }
    }
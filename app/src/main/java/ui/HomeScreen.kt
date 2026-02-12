package com.example.a438_project1.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    headerText: String = "Are You a Fan",
    artistHint: String = "Enter artist (ex: BashfortheWorld)",
    lyricsTitle: String = "Lyrics",
    lyricsText: String = "Tap Start to load lyrics",
    resultText: String = "",
    revealText: String = "",
    onStart: (artist: String) -> Unit = {},
    onReset: () -> Unit = {},
    onGuess: (guess: Int) -> Unit = {}
) {
    var artist by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = headerText,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Artist input
        OutlinedTextField(
            value = artist,
            onValueChange = { artist = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(artistHint) },
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        // Start / Reset row
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { onStart(artist.trim()) },
                modifier = Modifier.weight(1f)
            ) { Text("Start") }

            Spacer(Modifier.width(12.dp))

            Button(
                onClick = onReset,
                modifier = Modifier.weight(1f)
            ) { Text("Reset") }
        }

        // Lyrics section title
        Text(
            text = lyricsTitle,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 12.dp)
        )

        Spacer(Modifier.height(8.dp))

        // ScrollView equivalent that takes remaining space
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFFEEEEEE))
                .padding(12.dp)
        ) {
            val scroll = rememberScrollState()
            Text(
                text = lyricsText,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scroll)
                    .padding(top = 16.dp)
            )
        }

        // Result / reveal
        if (resultText.isNotBlank()) {
            Text(
                text = resultText,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            Spacer(Modifier.height(8.dp))
        }

        if (revealText.isNotBlank()) {
            Text(
                text = revealText,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        } else {
            Spacer(Modifier.height(12.dp))
        }

        // Answer buttons (1-4) in two rows
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { onGuess(1) },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            ) { Text("1") }

            Spacer(Modifier.width(12.dp))

            Button(
                onClick = { onGuess(2) },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            ) { Text("2") }
        }

        Spacer(Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { onGuess(3) },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            ) { Text("3") }

            Spacer(Modifier.width(12.dp))

            Button(
                onClick = { onGuess(4) },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            ) { Text("4") }
        }
    }
}

package com.example.a438_project1.ui.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameScreen(
    artistName: String,
    progressText: String,
    lyricsText: String,
    answers: List<String>, // size 4
    onAnswerClick: (index: Int) -> Unit,
    onNext: () -> Unit,
    onQuit: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // ======= TOP AREA (artist center + quit right) =======
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            Text(
                text = artistName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.TopCenter)
            )

            Button(
                onClick = onQuit,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text("Quit")
            }
        }

        // ======= MIDDLE AREA (progress + lyrics) =======
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .padding(top = 56.dp), // leaves space for top bar
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Progress with border (like editbox_background)
            Surface(
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                tonalElevation = 0.dp
            ) {
                Text(
                    text = progressText,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }

            // Lyrics box
            Surface(
                shape = RoundedCornerShape(8.dp),
                tonalElevation = 1.dp
            ) {
                Text(
                    text = lyricsText,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        // ======= BOTTOM AREA (answers + next) =======
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            answers.forEachIndexed { index, label ->
                Button(
                    onClick = { onAnswerClick(index) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(label)
                }
            }

            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Next")
            }
        }
    }
}
package com.example.a438_project1.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameScreen(
    artistName: String = "Artist Name",
    progressText: String = "Question 1 / 10",
    lyrics: String = "Lyrics will go here probably",
    answers: List<String> = listOf("Answer A", "Answer B", "Answer C", "Answer D"),
    onQuit: () -> Unit = {},
    onAnswerClick: (index: Int) -> Unit = {},
    onNext: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scroll = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top section (artist + quit)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.weight(1f))
            Text(
                text = artistName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(Modifier.weight(1f))
            OutlinedButton(
                onClick = onQuit,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Quit")
            }
        }

        // Middle content (progress + lyrics)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
                .padding(top = 56.dp) // pushes content below the header row
                .verticalScroll(scroll)
        ) {
            Spacer(Modifier.height(16.dp))

            // "editbox background" vibe: use an outlined style + padding
            OutlinedButton(
                onClick = { /* no-op */ },
                enabled = false,
                contentPadding = PaddingValues(8.dp)
            ) {
                Text(text = progressText, fontSize = 16.sp)
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = lyrics,
                fontSize = 20.sp,
                modifier = Modifier.padding(12.dp)
            )

            // leave space so bottom buttons don't overlap lyrics when scrolling
            Spacer(Modifier.height(220.dp))
        }

        // Bottom answers
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            answers.take(4).forEachIndexed { index, label ->
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

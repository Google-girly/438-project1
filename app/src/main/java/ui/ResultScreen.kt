package com.example.a438_project1.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResultsScreen(
    artistName: String,
    score: Int,
    total: Int,
    onRestart: () -> Unit,
    onGoHome: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Quiz Complete!", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))

        Text("Artist: $artistName", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))

        Text("Score: $score / $total", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onRestart,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Restart")
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = onGoHome,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Go Home")
        }
    }
}
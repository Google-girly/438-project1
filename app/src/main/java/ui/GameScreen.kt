package com.example.a438_project1.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GameScreen(
    artistName: String = "Artist Name",
    progressText: String = "Question 1 / 10",
    lyrics: String = "Lyrics will go here",
    answers: List<String> = listOf("Answer A", "Answer B", "Answer C", "Answer D"),


    selectedAnswerIndex: Int? = null,
    isCorrect: Boolean? = null,
    score: Int = 0,

    onQuit: () -> Unit = {},
    onAnswerClick: (index: Int) -> Unit = {},
    onNext: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val answered = selectedAnswerIndex != null

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top row: progress + score + quit
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(progressText, style = MaterialTheme.typography.titleMedium)
            Text("Score: $score", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(Modifier.height(8.dp))

        Text("Artist: $artistName", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(12.dp))

        // Lyrics card
        Card(Modifier.fillMaxWidth()) {
            Text(
                text = lyrics,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(Modifier.height(16.dp))


        answers.forEachIndexed { index, answer ->
            val isSelected = selectedAnswerIndex == index

            Button(
                onClick = { onAnswerClick(index) },
                enabled = !answered,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(answer)
            }
        }

        Spacer(Modifier.height(12.dp))


        if (answered) {
            Text(
                text = if (isCorrect == true) "Correct!" else "Wrong!",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(10.dp))

            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Next")
            }
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = onQuit,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Quit")
        }
    }
}

package com.example.a438_project1.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
    val pillShape = RoundedCornerShape(18.dp)
    val answerHeight = 54.dp

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top row: progress + score
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = progressText,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Surface(
                shape = RoundedCornerShape(999.dp),
                tonalElevation = 2.dp,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = "Score: $score",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        // artist name (w strong hierarchy)
        Text(
            text = artistName,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Select the correct song title",
            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )

        Spacer(Modifier.height(14.dp))

        // lyrics card (cleaner + more readable)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text(
                text = lyrics,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = MaterialTheme.typography.bodyLarge.lineHeight)
            )
        }

        Spacer(Modifier.height(16.dp))

        // Answer buttons
        answers.forEachIndexed { index, answer ->
            val isSelected = selectedAnswerIndex == index

            val containerColor =
                when {
                    !answered && isSelected -> MaterialTheme.colorScheme.secondaryContainer
                    !answered -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.70f)
                }

            Button(
                onClick = { onAnswerClick(index) },
                enabled = !answered,
                shape = pillShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(answerHeight)
                    .padding(vertical = 6.dp),
                colors = ButtonDefaults.buttonColors(containerColor = containerColor)
            ) {
                Text(
                    text = answer,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        // Feedback + Next
        if (answered) {
            val good = isCorrect == true
            val resultColor = if (good) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.errorContainer
            val resultTextColor = if (good) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onErrorContainer
            val resultText = if (good) "Correct!" else "Not quite — try the next one"

            Surface(
                color = resultColor,
                shape = RoundedCornerShape(999.dp),
            ) {
                Text(
                    text = resultText,
                    color = resultTextColor,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = pillShape
            ) {
                Text("Next", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium))
            }
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = onQuit,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = pillShape
        ) {
            Text("Quit", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium))
        }
    }
}

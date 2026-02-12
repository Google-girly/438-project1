package com.example.a438_project1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.a438_project1.ui.game.GameScreen
import androidx.compose.material3.MaterialTheme




class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                GameScreen(
                    artistName = "Artist Name",
                    progressText = "Question 1 / 10",
                    lyricsText = "Lyrics will go here probably",
                    answers = listOf("Answer A", "Answer B", "Answer C", "Answer D"),
                    onAnswerClick = { /* handle answer */ },
                    onNext = { /* next question */ },
                    onQuit = { finish() }
                )
            }
        }
    }
}
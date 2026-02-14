package com.example.a438_project1

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.example.a438_project1.ui.GameScreen

@Composable
fun GameRoute(onQuit: () -> Unit) {

    val context = LocalContext.current
    val activity = context as GameActivity
    val artistName = activity.intent.getStringExtra("artistName") ?: ""

    val vm: GameViewModel = viewModel(
        factory = GameViewModelFactory(
            itunesRepository = ApiProvider.itunesRepository,
            lyricsRep = ApiProvider.lyricsRep
        )
    )

    val state by vm.uiState.collectAsState()

    // ✅ THIS is where we start the game properly
    LaunchedEffect(artistName) {
        vm.startGame(artistName)
    }

    when {
        state.isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.error != null -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Error: ${state.error}")
            }
        }

        else -> {
            GameScreen(
                artistName = state.artistName,
                progressText = state.progressText,
                lyrics = state.lyricsText,
                answers = state.answers,
                selectedAnswerIndex = state.selectedAnswerIndex,
                isCorrect = state.isCorrect,
                score = state.score,
                onAnswerClick = { idx -> vm.submitAnswer(idx) },
                onNext = { vm.loadQuestion(next = true) },
                onQuit = onQuit
            )
        }
    }
}


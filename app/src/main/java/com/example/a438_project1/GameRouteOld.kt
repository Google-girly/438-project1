/*

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GameRouteOld(
    onQuit: () -> Unit
) {
    val vm: GameViewModel = viewModel(
        factory = GameViewModelFactory(
            itunesRepository = ApiProvider.itunesRepository,
            lyricsRep = ApiProvider.lyricsRep
        )
    )

    val state by vm.uiState.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadQuestion()
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
                lyricsText = state.lyricsText,
                answers = state.answers,
                onAnswerClick = { idx -> vm.submitAnswer(idx) }

*/

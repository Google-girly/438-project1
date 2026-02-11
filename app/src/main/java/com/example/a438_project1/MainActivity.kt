package com.example.a438_project1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.a438_project1.ui.theme._438_project1Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var correctSong: String? = null
    private var correctArtists: String? = null

    private val repo = LyricsRep(Network.lyricsApi)
    private val itunesRepo = ItunesRepository(Network.itunesApi)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            _438_project1Theme {
                MainScreen(
                    onStart = { artistQuery, setUi ->
                        // disable + show loading
                        setUi { it.copy(isLoading = true, lyricsText = "Loading songs + Lyrics..") }

                        lifecycleScope.launch {
                            try {
                                Log.d("QUIZ", "Calling iTunes.. ")
                                val option = itunesRepo.getFourSongs(artistQuery)
                                Log.d("QUIZ", "iTunes returned ${option.size} tracks")

                                val correct = option.random()
                                correctSong = correct.trackName
                                correctArtists = correct.artistName

                                val labels = option.map { it.trackName ?: "?" }

                                val lyrics = repo.fetchLyrics(
                                    correctSong ?: "",
                                    correctArtists ?: ""
                                )

                                setUi {
                                    it.copy(
                                        isLoading = false,
                                        answerLabels = labels,
                                        lyricsText = lyrics.lines().take(8).joinToString("\n")
                                    )
                                }
                            } catch (e: Exception) {
                                Log.d("QUIZ", "Failed loading songs/lyrics", e)
                                setUi {
                                    it.copy(
                                        isLoading = false,
                                        lyricsText = "Error: ${e.message}"
                                    )
                                }
                            }
                        }
                    },
                    onReset = { setUi ->
                        setUi {
                            it.copy(
                                resultText = "",
                                revealText = "",
                                lyricsText = "Tap start to load lyrics"
                            )
                        }
                    },
                    onGuess = { guessText, setUi ->
                        val correct = guessText.equals(correctSong ?: "", ignoreCase = true)
                        setUi {
                            it.copy(
                                resultText = if (correct) "Correct" else "Wrong",
                                revealText = "Correct answer : ${correctSong ?: "?"}- ${correctArtists ?: "?"}"
                            )
                        }
                    }
                )
            }
        }
    }
}

private data class MainUiState(
    val artist: String = "",
    val lyricsText: String = "Tap start to load lyrics",
    val resultText: String = "",
    val revealText: String = "",
    val answerLabels: List<String> = listOf("1", "2", "3", "4"),
    val isLoading: Boolean = false
)

@Composable
private fun MainScreen(
    onStart: (artistQuery: String, setUi: ((MainUiState) -> MainUiState) -> Unit) -> Unit,
    onReset: (setUi: ((MainUiState) -> MainUiState) -> Unit) -> Unit,
    onGuess: (guessText: String, setUi: ((MainUiState) -> MainUiState) -> Unit) -> Unit
) {
    var ui by remember { mutableStateOf(MainUiState()) }
    val setUi: (((MainUiState) -> MainUiState) -> Unit) = { updater -> ui = updater(ui) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Are You a Fan",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Artist input
        OutlinedTextField(
            value = ui.artist,
            onValueChange = { setUi { s -> s.copy(artist = it) } },
            enabled = !ui.isLoading,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Enter artist (ex: BashfortheWorld)") },
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        // Start / Reset buttons
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    setUi { s -> s.copy(resultText = "", revealText = "") }

                    val artistQuery = ui.artist.trim()
                    if (artistQuery.isBlank()) {
                        setUi { s -> s.copy(lyricsText = "Please type an artist name first") }
                        return@Button
                    }

                    Log.d("QUIZ", "Start clicked. artistQuery =$artistQuery ")
                    onStart(artistQuery, setUi)
                },
                enabled = !ui.isLoading,
                modifier = Modifier.weight(1f)
            ) { Text("Start") }

            Spacer(Modifier.width(12.dp))

            Button(
                onClick = { onReset(setUi) },
                enabled = !ui.isLoading,
                modifier = Modifier.weight(1f)
            ) { Text("Reset") }
        }

        // Lyrics title
        Text(
            text = "Lyrics",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 12.dp)
        )

        Spacer(Modifier.height(8.dp))

        // Scrollable lyrics box (like ScrollView + gray background)
        val scroll = rememberScrollState()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFFEEEEEE))
                .padding(12.dp)
        ) {
            Text(
                text = ui.lyricsText,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scroll)
                    .padding(top = 16.dp)
            )
        }

        Spacer(Modifier.height(8.dp))

        // Result / reveal
        if (ui.resultText.isNotBlank()) {
            Text(text = ui.resultText, fontWeight = FontWeight.Bold)
        }
        if (ui.revealText.isNotBlank()) {
            Text(text = ui.revealText, modifier = Modifier.padding(bottom = 12.dp))
        } else {
            Spacer(Modifier.height(12.dp))
        }

        // Answer buttons 1-4
        AnswerRow(
            left = ui.answerLabels.getOrNull(0) ?: "1",
            right = ui.answerLabels.getOrNull(1) ?: "2",
            enabled = !ui.isLoading,
            onLeft = { onGuess(ui.answerLabels.getOrNull(0) ?: "1", setUi) },
            onRight = { onGuess(ui.answerLabels.getOrNull(1) ?: "2", setUi) }
        )

        Spacer(Modifier.height(12.dp))

        AnswerRow(
            left = ui.answerLabels.getOrNull(2) ?: "3",
            right = ui.answerLabels.getOrNull(3) ?: "4",
            enabled = !ui.isLoading,
            onLeft = { onGuess(ui.answerLabels.getOrNull(2) ?: "3", setUi) },
            onRight = { onGuess(ui.answerLabels.getOrNull(3) ?: "4", setUi) }
        )
    }
}

@Composable
private fun AnswerRow(
    left: String,
    right: String,
    enabled: Boolean,
    onLeft: () -> Unit,
    onRight: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = onLeft,
            enabled = enabled,
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
        ) { Text(left) }

        Spacer(Modifier.width(12.dp))

        Button(
            onClick = onRight,
            enabled = enabled,
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
        ) { Text(right) }
    }
}

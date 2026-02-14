package com.example.a438_project1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.min

data class GameUiState(
    val artistName: String = "",
    val trackName: String = "",
    val progressText: String = "Question 1 / 10",
    val lyricsText: String = "",
    val answers: List<String> = emptyList(),
    val selectedAnswerIndex: Int? = null,
    val isCorrect: Boolean? = null,
    val score: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

class GameViewModel(
    private val itunesRepository: ItunesRepository,
    private val lyricsRep: LyricsRep
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState(isLoading = true))
    val uiState: StateFlow<GameUiState> = _uiState

    private var questionNum = 1
    private val totalQuestions = 10

    private var answeredThisQuestion = false

    // ✅ the chosen artist from Home
    private var chosenArtist: String? = null

    /** Call this ONCE from GameActivity after reading the Intent extra. */
    fun startGame(artist: String) {
        chosenArtist = artist
        questionNum = 1
        _uiState.value = GameUiState(
            isLoading = true,
            progressText = "Question 1 / $totalQuestions"
        )
        loadQuestion(next = false)
    }

    fun loadQuestion(next: Boolean = false) {
        val artist = chosenArtist
        if (artist.isNullOrBlank()) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = "No artist selected. Go back and pick an artist."
            )
            return
        }

        if (next) questionNum = (questionNum + 1).coerceAtMost(totalQuestions)
        answeredThisQuestion = false

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                selectedAnswerIndex = null,
                isCorrect = null,
                progressText = "Question $questionNum / $totalQuestions"
            )

            try {
                // ✅ get four song options for the SAME artist (your repo already does this)
                val tracks = itunesRepository.getFourSongs(artist)

                // build answers = 4 song titles
                val answers = tracks.mapNotNull { it.trackName }.distinct()
                if (answers.size < 4) throw IllegalStateException("Not enough unique songs for: $artist")

                // pick which of the 4 is correct
                val correctTitle = answers.random()

                // fetch lyrics for the correct song
                val lyricsFull = lyricsRep.fetchLyrics(song = correctTitle, artist = artist)

                // ✅ show only a snippet (keeps UI readable)
                val lyricsSnippet = lyricsFull
                    .replace("\r\n", "\n")
                    .trim()
                    .let { snippet(it, maxChars = 220) }

                _uiState.value = _uiState.value.copy(
                    artistName = artist,
                    trackName = correctTitle,
                    lyricsText = lyricsSnippet,
                    answers = answers.shuffled(),
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load question"
                )
            }
        }
    }

    fun submitAnswer(index: Int) {
        if (answeredThisQuestion) return
        answeredThisQuestion = true

        val current = _uiState.value
        val chosen = current.answers.getOrNull(index) ?: return
        val correct = current.trackName

        val isCorrect = (chosen == correct)
        val newScore = if (isCorrect) current.score + 1 else current.score

        _uiState.value = current.copy(
            selectedAnswerIndex = index,
            isCorrect = isCorrect,
            score = newScore
        )
    }

    private fun snippet(text: String, maxChars: Int): String {
        if (text.isBlank()) return "No lyrics found."
        val cleaned = text.replace(Regex("\\s+"), " ").trim()
        val end = min(cleaned.length, maxChars)
        return if (cleaned.length <= maxChars) cleaned else cleaned.substring(0, end) + "…"
    }
}



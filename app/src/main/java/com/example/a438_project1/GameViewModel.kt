package com.example.a438_project1.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a438_project1.ItunesRepository
import com.example.a438_project1.LyricsRep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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

    // small artist pool to sample wrong answers from (you can expand)
    private val artistPool = listOf(
        "Taylor Swift", "Drake", "The Weeknd", "Adele",
        "Bad Bunny", "Ed Sheeran", "Billie Eilish", "Bruno Mars",
        "Kendrick Lamar", "Rihanna"
    )

    // internal guards
    private var answeredThisQuestion = false

    fun loadQuestion(next: Boolean = false) {
        if (next) questionNum = (questionNum + 1).coerceAtMost(totalQuestions)

        // reset per-question internal state
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
                // pick an artist (random from pool)
                val chosenArtist = artistPool.random()

                // use your repository to get four songs for that artist
                val tracks = itunesRepository.getFourSongs(chosenArtist)

                // choose one track as the correct one
                val correctTrack = tracks.random()
                val artistName = correctTrack.artistName ?: chosenArtist
                val trackName = correctTrack.trackName ?: throw IllegalStateException("Missing track name")

                // fetch lyrics (LyricsRep handles sanitizing)
                val lyrics = lyricsRep.fetchLyrics(song = trackName, artist = artistName)

                // build 3 wrong artists (from artistPool) + the correct artist
                val wrongArtists = artistPool.filter { it != artistName }.shuffled().take(3)
                val answers = (wrongArtists + artistName).shuffled()

                _uiState.value = _uiState.value.copy(
                    artistName = artistName,
                    trackName = trackName,
                    lyricsText = lyrics,
                    answers = answers,
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
        val correct = current.artistName

        val isCorrect = (chosen == correct)
        val newScore = if (isCorrect) current.score + 1 else current.score

        _uiState.value = current.copy(
            selectedAnswerIndex = index,
            isCorrect = isCorrect,
            score = newScore,
            // optionally use error field for messages only when something goes wrong:
            error = null
        )
    }
}



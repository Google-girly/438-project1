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
    val error: String? = null,
    val isGameOver: Boolean = false
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
    private var chosenArtist: String? = null

    fun startGame(artist: String) {
        chosenArtist = artist
        questionNum = 1
        answeredThisQuestion = false

        _uiState.value = GameUiState(
            isLoading = true,
            progressText = "Question 1 / $totalQuestions",
            isGameOver = false
        )

        loadQuestion(next = false)
    }

    fun restartGame() {
        val artist = chosenArtist
        if (!artist.isNullOrBlank()) startGame(artist)
    }

    /**
     *
     * It will go to results screen after question 10.
     */
    fun onNextPressed() {
        if (questionNum >= totalQuestions) {
            _uiState.value = _uiState.value.copy(
                isGameOver = true,
                isLoading = false,
                error = null
            )
            return
        }
        loadQuestion(next = true)
    }

    private fun loadQuestion(next: Boolean = false) {
        val artist = chosenArtist
        if (artist.isNullOrBlank()) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = "No artist selected."
            )
            return
        }

        if (next) questionNum += 1
        answeredThisQuestion = false

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                selectedAnswerIndex = null,
                isCorrect = null,
                progressText = "Question $questionNum / $totalQuestions",
                isGameOver = false
            )

            try {
                var correctTitle: String? = null
                var lyricsFull: String? = null
                var chosenAnswers: List<String> = emptyList()

                var attempts = 0
                val maxAttempts = 25

                while (correctTitle == null && attempts < maxAttempts) {
                    attempts++

                    val tracks = itunesRepository.getFourSongs(artist)

                    val titles = tracks
                        .mapNotNull { it.trackName }
                        .map { it.trim() }
                        .filter { it.isNotBlank() }
                        .distinct()

                    if (titles.size < 4) continue

                    val shuffled = titles.shuffled()
                    chosenAnswers = shuffled

                    for (candidate in shuffled) {
                        val candidateLyrics = fetchLyricsWithVariants(songTitle = candidate, artist = artist)
                        if (candidateLyrics != null) {
                            correctTitle = candidate
                            lyricsFull = candidateLyrics
                            break
                        }
                    }
                }

                if (correctTitle == null || lyricsFull == null || chosenAnswers.isEmpty()) {
                    throw IllegalStateException(
                        "Lyrics not available right now. Tap Next to try again."
                    )
                }

                val lyricsSnippet = snippet(lyricsFull!!, maxChars = 220)

                _uiState.value = _uiState.value.copy(
                    artistName = artist,
                    trackName = correctTitle!!,
                    lyricsText = lyricsSnippet,
                    answers = chosenAnswers,
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

        val correctBool = (chosen == correct)
        val newScore = if (correctBool) current.score + 1 else current.score

        _uiState.value = current.copy(
            selectedAnswerIndex = index,
            isCorrect = correctBool,
            score = newScore
        )
    }

    private fun looksLikeMissingLyrics(text: String): Boolean {
        val t = text.trim().lowercase()
        return t.isBlank() ||
                t.contains("404") ||
                t.contains("not found") ||
                t.contains("no lyrics") ||
                t.contains("error") ||
                t.startsWith("{") ||
                t.startsWith("no lyrics found")
    }

    private fun titleVariants(title: String): List<String> {
        val t = title.trim()
        val variants = mutableListOf<String>()

        variants += t
        variants += t.replace(Regex("\\s*[\\(\\[].*?[\\)\\]]\\s*"), " ").trim()
        variants += t.split(" - ", " – ", " — ", ": ").firstOrNull()?.trim().orEmpty()
        variants += t.replace(Regex("(?i)\\s*(feat\\.|ft\\.)\\s+.*$"), "").trim()
        variants += t.replace(
            Regex("(?i)\\s*(remaster(ed)?|live|radio edit|clean|explicit|remix|version)\\s*$"),
            ""
        ).trim()
        variants += t.replace(Regex("[^A-Za-z0-9\\s]"), " ")
            .replace(Regex("\\s+"), " ")
            .trim()

        return variants.map { it.trim() }.filter { it.isNotBlank() }.distinct()
    }


    private suspend fun fetchLyricsWithVariants(songTitle: String, artist: String): String? {
        for (v in titleVariants(songTitle)) {
            val res: String = try {
                lyricsRep.fetchLyrics(song = v, artist = artist)
            } catch (_: Exception) {
                continue
            }
            if (!looksLikeMissingLyrics(res)) return res
        }
        return null
    }

    private fun snippet(text: String, maxChars: Int): String {
        if (text.isBlank()) return "No lyrics found."

        var cleaned = text.replace("\r\n", "\n").trim()
        cleaned = cleaned.replace(
            Regex("^paroles de la chanson.*?\\n", RegexOption.IGNORE_CASE),
            ""
        )
        cleaned = cleaned.replace(
            Regex("^lyrics of.*?\\n", RegexOption.IGNORE_CASE),
            ""
        )
        cleaned = cleaned.replace(Regex("\\s+"), " ").trim()

        val end = min(cleaned.length, maxChars)
        return if (cleaned.length <= maxChars) cleaned else cleaned.substring(0, end) + "…"
    }
}



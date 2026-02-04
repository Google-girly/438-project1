package com.example.a438_project1
import android.util.Log

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.lifecycleScope

import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(){
    private var correctSong: String? = null
    private var correctArtists: String? = null
    //private val choices = listOf("XXX.", "PRIDE." ,"FEAR.","These Walls")

    private val repo = LyricsRep(Network.lyricsApi)
    private val itunesRepo = ItunesRepository(Network.itunesApi)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

// Tell Android to use the XML layout file
        setContentView(R.layout.activity_main)
        val etArtists = findViewById<EditText>(R.id.etArtist)
        val tvLyrics = findViewById<TextView>(R.id.tvLyrics)
        requireNotNull(tvLyrics){"tvLyrics missing from activity_main.xml"}
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val tvReveal = findViewById<TextView>(R.id.tvReveal)

        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnReset = findViewById<Button>(R.id.btnReset)

        val btn1 = findViewById<Button>(R.id.btn1)
        val btn2 = findViewById<Button>(R.id.btn2)
        val btn3 = findViewById<Button>(R.id.btn3)
        val btn4 = findViewById<Button>(R.id.btn4)
        val answerButton = listOf(btn1, btn2, btn3,btn4)


        fun clearResult(){
            tvResult.text = ""
            tvReveal.text = ""
        }
        fun setButtonEnabled(enabled: Boolean){
            btnStart.isEnabled = enabled
            btnReset. isEnabled = enabled
            answerButton.forEach{it.isEnabled = enabled}
        }


        //reset section
        btnStart.setOnClickListener {
            clearResult()

            val artistQuery = etArtists.text.toString().trim()
            if(artistQuery.isBlank()){
                tvLyrics.text = "Please type an artist name first"
                return@setOnClickListener
            }
            tvLyrics.text = "Loading songs + Lyrics.."
            setButtonEnabled(false)

            Log.d("QUIZ", "Start clicked. artistQuery =$artistQuery ")


            lifecycleScope.launch {
                try{
                    Log.d("QUIZ", "Calling iTunes.. ")
                    val option = itunesRepo.getFourSongs(artistQuery)
                    Log.d("QUIZ", "iTunes returned ${option.size} tracks")

                    val correct = option.random()
                    correctSong = correct.trackName
                    correctArtists = correct.artistName

                    answerButton.zip(option).forEach { (btn, track)->
                        btn.text = track.trackName ?: "?"
                    }

                    val lyrics = repo.fetchLyrics(
                        correctSong!!,
                        correctArtists!!
                    )
                    tvLyrics.text = lyrics.lines().take(8).joinToString("\n")

                }catch (e: Exception){
                    Log.d("QUIZ", "Failed loading songs/lyrics", e)
                    tvLyrics.text = "Error: ${e.message}"
                }finally {
                    setButtonEnabled(true)
                }

            }
        }
        btnReset.setOnClickListener {
            clearResult()
            tvLyrics.text = "Tap start to load lyrics"
        }
        fun choose(answer: String){
            val correct = answer.equals(correctSong ?: "", ignoreCase = true)
            tvResult.text = if(correct)"Correct" else "Wrong"
            tvReveal.text = "Correct answer : ${correctSong?: "?"}- ${correctArtists ?: "?"}"
        }
        //Answer button
        btn1.setOnClickListener { choose(btn1.text.toString()) }
        btn2.setOnClickListener { choose(btn2.text.toString()) }
        btn3.setOnClickListener { choose(btn3.text.toString()) }
        btn4.setOnClickListener { choose(btn4.text.toString()) }


    }

}
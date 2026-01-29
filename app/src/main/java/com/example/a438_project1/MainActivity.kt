package com.example.a438_project1

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.a438_project1.ui.theme._438_project1Theme
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(){
    private val correctSong = "XXX."
    private val correctArtists = "Kendrick Lamar"
    private val choices = listOf("Lust.", "PRIDE." ,"FEAR","These Walls")

    private val repo = LyricsRep(Network.lyricsApi)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

// Tell Android to use the XML layout file
        setContentView(R.layout.activity_main)

        val tvLyrics = findViewById<TextView>(R.id.tvLyricsTitle)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val tvReveal = findViewById<TextView>(R.id.Reveal)

        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnReset = findViewById<Button>(R.id.btnReset)

        val btn1 = findViewById<Button>(R.id.btn1)
        val btn2 = findViewById<Button>(R.id.btn2)
        val btn3 = findViewById<Button>(R.id.btn3)
        val btn4 = findViewById<Button>(R.id.btn4)

        //setButton label
        btn1.text = choices[0]
        btn2.text = choices[1]
        btn3.text = choices[2]
        btn4.text = choices[3]

        fun clearResult(){
            tvResult.text = ""
            tvReveal.text = ""
        }
        btnStart.setOnClickListener {
            clearResult()
            tvLyrics.text= "Loading Lyrics..."
            lifecycleScope.launch {
                try {
                    val lyrics = repo.fetchLyrics(correctSong, correctArtists)
                    //will only show 8 lines
                    val shortened = lyrics.lines().take(8).joinToString("\n")
                    tvLyrics.text = shortened
                } catch (e: Exception){
                    tvLyrics.text = "Error loading lyrics: ${e.message}"
                }
            }
        }
        //reset section
        btnReset.setOnClickListener {
            clearResult()
        }
        fun choose(answer: String){
            val correct = answer.equals(correctSong, ignoreCase = true)
            tvResult.text = if(correct)"Correct" else "Wrong"
            tvReveal.text = "Correct answer : $correctSong-$correctArtists"
        }
        //Answer button
        btn1.setOnClickListener { choose(btn1.text.toString()) }
        btn2.setOnClickListener { choose(btn2.text.toString()) }
        btn3.setOnClickListener { choose(btn3.text.toString()) }
        btn4.setOnClickListener { choose(btn4.text.toString()) }


    }

}
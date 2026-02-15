package com.example.a438_project1

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.a438_project1.ui.HomeScreen

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HomeScreen(
                itunesApi = ApiProvider.itunesApi,
                onStartGame = { artistName ->
                    val intent = Intent(this, GameActivity::class.java)
                    intent.putExtra("artistName", artistName)
                    startActivity(intent)
                }
            )
        }
    }
}

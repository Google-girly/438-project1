package com.example.a438_project1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.example.a438_project1.ui.LoginScreen
import com.example.a438_project1.ui.theme._438_project1Theme

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            _438_project1Theme {
                Surface {
                    LoginScreen()
                }
            }
        }
    }
}

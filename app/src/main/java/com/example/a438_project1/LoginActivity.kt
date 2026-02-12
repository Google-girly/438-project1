package com.example.a438_project1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.lifecycle.lifecycleScope
import com.example.a438_project1.data.AppDatabase
import com.example.a438_project1.data.User
import com.example.a438_project1.ui.LoginScreen
import com.example.a438_project1.ui.theme._438_project1Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()

        setContent {
            _438_project1Theme {
                Surface {
                    LoginScreen(
                        onLoginClick = {
                            login, password ->
                            lifecycleScope.launch(Dispatchers.IO) {
                                val user = userDao.getUser(login, password)
                                withContext(Dispatchers.Main) {
                                    if (user != null) {
                                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(this@LoginActivity, "Invalid login or password", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        },
                        onCreateClick = { email, username, password ->
                            lifecycleScope.launch(Dispatchers.IO) {
                                val existingUser = userDao.getUserByUsername(username)
                                if (existingUser != null) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(this@LoginActivity, "Username already exists", Toast.LENGTH_SHORT).show()
                                    }
                                    return@launch
                                }

                                val user = User(name = email, username = username, password = password)
                                userDao.insert(user)
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(this@LoginActivity, "Account created successfully", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

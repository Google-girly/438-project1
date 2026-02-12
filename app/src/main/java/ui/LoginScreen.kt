package com.example.a438_project1.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginClick: (login: String, password: String) -> Unit = { _, _ -> },
    onCreateClick: (email: String, username: String, password: String) -> Unit = { _, _, _ -> }
) {
    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var createMode by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .imePadding()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header (matches: textSize 28sp, bold, centered, marginTop 32dp)
        Spacer(Modifier.height(32.dp))
        Text(
            text = if (createMode) "Create Account" else "Are You a Fan?",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        // Login input (matches: marginTop 48dp, full width)
        Spacer(Modifier.height(48.dp))
        if (createMode) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Username") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
        } else {
            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email or username") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )
        }

        // Password input (matches: marginTop 16dp, full width)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )

        // Login button (matches: marginTop 32dp, centered)
        Spacer(Modifier.height(32.dp))
        if (createMode) {
            Button(
                onClick = {
                    focusManager.clearFocus()
                    onCreateClick(email.trim(), username.trim(), password)
                }
            ) {
                Text("Create Account")
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { createMode = false }
            ) {
                Text("Back to Login")
            }
        } else {
            Row {
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        onLoginClick(login.trim(), password)
                    }
                ) {
                    Text("Login")
                }
                Spacer(Modifier.width(16.dp))
                Button(
                    onClick = { createMode = true }
                ) {
                    Text("Create Account")
                }
            }
        }
    }
}
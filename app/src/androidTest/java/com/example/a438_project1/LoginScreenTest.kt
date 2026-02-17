package com.example.a438_project1

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.a438_project1.ui.LoginScreen
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginButtonDisabledWhenFieldsEmpty() {
        composeTestRule.setContent {
            LoginScreen()
        }

        composeTestRule
            .onNodeWithText("Login")
            .assertIsNotEnabled()
    }

    @Test
    fun loginCallsOnLoginClickWithCorrectValues() {
        var loginCalled = false
        var passedLogin = ""
        var passedPassword = ""

        composeTestRule.setContent {
            LoginScreen(
                onLoginClick = { login, password ->
                    loginCalled = true
                    passedLogin = login
                    passedPassword = password
                }
            )
        }

        composeTestRule.onNodeWithText("Email or username")
            .performTextInput("test@example.com")

        composeTestRule.onNodeWithText("Password")
            .performTextInput("password123")

        composeTestRule.onNodeWithText("Login")
            .performClick()

        Assert.assertTrue(loginCalled)
        Assert.assertEquals("test@example.com", passedLogin)
        Assert.assertEquals("password123", passedPassword)
    }

    @Test
    fun switchingToCreateModeShowsCreateFields() {
        composeTestRule.setContent {
            LoginScreen()
        }

        composeTestRule
            .onNodeWithText("Create an Account")
            .performClick()

        composeTestRule
            .onNodeWithText("Email")
            .assertExists()

        composeTestRule
            .onNodeWithText("Username")
            .assertExists()

        composeTestRule
            .onNodeWithText("Create Account")
            .assertExists()
    }

    @Test
    fun createCallsOnCreateClickWithCorrectValues() {
        var createCalled = false
        var emailPassed = ""
        var usernamePassed = ""
        var passwordPassed = ""

        composeTestRule.setContent {
            LoginScreen(
                onCreateClick = { email, username, password ->
                    createCalled = true
                    emailPassed = email
                    usernamePassed = username
                    passwordPassed = password
                }
            )
        }

        composeTestRule.onNodeWithText("Create an Account").performClick()

        composeTestRule.onNodeWithText("Email")
            .performTextInput("new@example.com")

        composeTestRule.onNodeWithText("Username")
            .performTextInput("newuser")

        composeTestRule.onNodeWithText("Password")
            .performTextInput("password123")

        composeTestRule.onNodeWithText("Create Account")
            .performClick()

        Assert.assertTrue(createCalled)
        Assert.assertEquals("new@example.com", emailPassed)
        Assert.assertEquals("newuser", usernamePassed)
        Assert.assertEquals("password123", passwordPassed)
    }
}
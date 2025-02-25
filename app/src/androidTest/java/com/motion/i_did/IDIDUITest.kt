package com.motion.i_did

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.motion.i_did.repository.AuthRepository
import com.motion.i_did.ui.login.LoginScreen
import com.motion.i_did.ui.splash.SplashScreen
import org.junit.Rule
import org.junit.Test

//UI test for the app
class IDIDUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    //UI test for splash screen
    @Test
    fun testSplashUI() {
        composeTestRule.setContent {
            val context = LocalContext.current
            val navController = rememberNavController()
            val authRepository = AuthRepository(context)
            SplashScreen(navController, authRepository)
        }
        composeTestRule.waitForIdle()

        // Verifying the splash screen UI elements
        composeTestRule.onNodeWithText("I-DID").assertExists()

    }
    //UI test for login screen
    @Test
    fun testLoginUI() {
        composeTestRule.setContent {
            val context = LocalContext.current
            val navController = rememberNavController()
            val authRepository = AuthRepository(context)
            LoginScreen(navController, authRepository)
        }
        composeTestRule.waitForIdle()
        // Verifying the Login screen UI elements
        composeTestRule.onNodeWithText("Email").assertExists()
        composeTestRule.onNodeWithText("Password").assertExists()
        composeTestRule.onNodeWithText("Need an account? Register").assertExists()
        composeTestRule.onNodeWithText("Login").performClick()
    }
}

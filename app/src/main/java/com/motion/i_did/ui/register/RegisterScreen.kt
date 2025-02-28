package com.motion.i_did.ui.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.motion.i_did.core.routes.AppRoutes
import com.motion.i_did.repository.AuthRepository
import com.motion.i_did.ui.theme.LightBlue300
import com.motion.i_did.ui.theme.LightBlue50
import com.motion.i_did.ui.theme.LightBlue500
import com.motion.i_did.ui.theme.LightBlue600
import kotlinx.coroutines.launch


// Register screen for the app
@Composable
fun RegisterScreen(
    navController: NavController,
    authRepository: AuthRepository
) {
    //variable for storing the email
    var email by remember { mutableStateOf("") }
    //variable for storing the password
    var password by remember { mutableStateOf("") }
    //variable for storing the confirmed password
    var confirmPassword by remember { mutableStateOf("") }
    //variable for storing the error message
    var errorMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    //Column for the email registration
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Text field for the email
        TextField(
            colors = TextFieldDefaults.colors(
                focusedLabelColor = LightBlue300,
                focusedIndicatorColor = LightBlue300,
                unfocusedIndicatorColor = LightBlue500
            ),
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Text field for the password
        TextField(
            colors = TextFieldDefaults.colors(
                focusedLabelColor = LightBlue300,
                focusedIndicatorColor = LightBlue300,
                unfocusedIndicatorColor = LightBlue500
            ),
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Text field for the confirmed password
        TextField(
            colors = TextFieldDefaults.colors(
                focusedLabelColor = LightBlue300,
                focusedIndicatorColor = LightBlue300,
                unfocusedIndicatorColor = LightBlue500
            ),
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()

        )

        // Error Message for the registration screen
        if (errorMessage.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Register Button
        Button(
            onClick = {
                errorMessage = ""

                // Validation
                when {
                    email.isBlank() -> {
                        errorMessage = "Email cannot be empty"
                        return@Button
                    }
                    password.length < 6 -> {
                        errorMessage = "Password must be at least 6 characters"
                        return@Button
                    }
                    password != confirmPassword -> {
                        errorMessage = "Passwords do not match"
                        return@Button
                    }
                }

                // Registration
                scope.launch {

                    val result = authRepository.registerWithEmailPassword(email, password)
                    result.onSuccess { id ->
                        //navigation for going to the home screen
                        navController.navigate(AppRoutes.Home.route) {
                            popUpTo(AppRoutes.Register.route) { inclusive = true }
                        }
                    }.onFailure {exception ->
                        errorMessage = exception.localizedMessage ?: "Register Failed"
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = LightBlue600,
                contentColor = LightBlue50
            ),
            modifier = Modifier.fillMaxWidth()

        ) {
            Text("Register")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // button for going back to the login screen
        TextButton(
            onClick = {
                //Navigation for going back to the login screen
                navController.navigateUp()
            }
        ) {
            Text("Already have an account? Login")
        }
    }
}
package com.motion.i_did.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.motion.i_did.R
import com.motion.i_did.core.routes.AppRoutes
import com.motion.i_did.data.local.database.FavoriteDatabase
import com.motion.i_did.data.local.repository.FavoriteRepository
import com.motion.i_did.repository.AuthRepository
import com.motion.i_did.ui.theme.LightBlue300
import com.motion.i_did.ui.theme.LightBlue50
import com.motion.i_did.ui.theme.LightBlue500
import com.motion.i_did.ui.theme.LightBlue600
import com.motion.i_did.ui.theme.LightBlue900
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//Login Screen for the app
@Composable
fun LoginScreen(
    navController: NavController,
    authRepository: AuthRepository
){
    val context = LocalContext.current
    //variables for storing the email
    var email by remember { mutableStateOf("") }
    //variables for storing the password
    var password by remember { mutableStateOf("") }
    //variables for storing the visibility of the password
    var passwordVisible by remember { mutableStateOf(false) }
    //variables for storing the error message
    var errorMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    //value for favorite database and repository
    val database = remember { FavoriteDatabase.getDatabase(context) }
    val favoriteRepository = remember { FavoriteRepository.getInstance(database.favoriteDao()) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = {
            //logo app
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.idid),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(500.dp)
                        .clip(RoundedCornerShape(16.dp))

                )
            }
            //Email and password input column
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
            ){
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = email,
                    onValueChange = {email = it},
                    label = {Text("Email")},
                    colors = TextFieldDefaults.colors(
                        focusedLabelColor = LightBlue300,
                        focusedIndicatorColor = LightBlue300,
                        unfocusedIndicatorColor = LightBlue500
                    ),
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    onValueChange = {password = it},
                    label = {Text("Password")},
                    colors = TextFieldDefaults.colors(
                        focusedLabelColor = LightBlue300,
                        focusedIndicatorColor = LightBlue300,
                        unfocusedIndicatorColor = LightBlue500
                    ),
                    //setting for password visibility
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon =
                            if (passwordVisible) R.drawable.visibility_off_24dp else R.drawable.visibility_24dp
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(id = icon),
                                contentDescription = "Toggle Password Visibility"
                            )
                        }
                    }
                )
                // Error Message for Login Screen
                if (errorMessage.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(modifier =Modifier.height(24.dp))
                //Login Button
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        Toast.makeText(
                            context,
                            "Login button clicked",
                            Toast.LENGTH_SHORT
                        ).show()
                        errorMessage = ""
                        scope.launch {
                            val result = authRepository.signInWithEmailPassword(email, password)
                            result.onSuccess { id ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    //Deleting favorite repository of the previous user
                                    favoriteRepository.deleteALL()
                                }
                                //navigation for going to the input note screen
                                navController.navigate(AppRoutes.Home.route){
                                    popUpTo(AppRoutes.Login.route){ inclusive = true }
                                }
                            }.onFailure { exception ->
                                errorMessage = exception.localizedMessage ?: "Login Failed"
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LightBlue600,
                        contentColor = LightBlue50
                    ),

                ) {
                    Text(
                        text = "Login",
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                //Register button
                TextButton(

                    onClick = {
                        // navigation for going to the register  screen
                        navController.navigate(AppRoutes.Register.route)
                        }
                    ) {
                    Text("Don't have an account? Register here",
                        color = LightBlue900
                    )
                }
            }
        }
    )
}


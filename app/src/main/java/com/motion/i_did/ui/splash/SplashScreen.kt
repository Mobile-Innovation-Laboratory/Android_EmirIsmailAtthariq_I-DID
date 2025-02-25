package com.motion.i_did.ui.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.motion.i_did.core.routes.AppRoutes
import com.motion.i_did.repository.AuthRepository
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    authRepository: AuthRepository
){
    LaunchedEffect(Unit) {
        delay(1500)


        if (authRepository.isUserLoggedIn()){
            //navigate to home
            navController.navigate(AppRoutes.Home.route){
                popUpTo(AppRoutes.Splash.route) {inclusive = true}
            }
        }else {
            // Navigate to login
            navController.navigate(AppRoutes.Login.route) {
                popUpTo(AppRoutes.Splash.route) { inclusive = true }
            }
        }
    }
    // Splash screen UI
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center ,
                horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "I-DID",
            style = MaterialTheme.typography.headlineLarge
        )

        CircularProgressIndicator(
            modifier = Modifier.padding(20.dp)
        )
    }

}
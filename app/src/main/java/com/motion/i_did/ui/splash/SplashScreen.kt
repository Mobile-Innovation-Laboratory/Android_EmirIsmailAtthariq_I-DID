package com.motion.i_did.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.motion.i_did.R
import com.motion.i_did.core.routes.AppRoutes
import com.motion.i_did.repository.AuthRepository
import com.motion.i_did.ui.theme.LightBlue300
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
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center ,
                horizontalAlignment = Alignment.CenterHorizontally
    ) {


        CircularProgressIndicator(
            modifier = Modifier.padding(20.dp),
                color = LightBlue300
        )
    }

}
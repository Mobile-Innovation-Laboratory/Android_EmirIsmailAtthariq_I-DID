package com.motion.i_did.ui.Profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.motion.i_did.core.routes.AppRoutes
import com.motion.i_did.data.Quote
import com.motion.i_did.data.RetrofitInstance
import com.motion.i_did.repository.AuthRepository
import com.motion.i_did.ui.theme.IDIDTheme
import com.motion.i_did.ui.theme.LightBlue300
import com.motion.i_did.ui.theme.LightBlue400
import com.motion.i_did.ui.theme.LightBlue50
import com.motion.i_did.ui.theme.LightBlue600

//Profile Screen for the app
@Composable
fun ProfileScreen(
    navController: NavController,
    authRepository: AuthRepository
) {
    IDIDTheme {
        Scaffold(
            //bottom bar for the profile screen
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ) { NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                         contentDescription = null
                        )
                    },
                        label = {
                            Text(
                            "Home"
                        )
                    },
                    selected = false,
                    onClick = {
                    //navigation for going to the home screen
                    navController.navigate(AppRoutes.Home.route){
                        launchSingleTop = true
                         }
                    }
                )
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(
                                "Favorite"
                            )
                        },
                        selected = false,
                        onClick = {
                            //navigation for going to the favorite screen
                            navController.navigate(AppRoutes.Favorite.route){
                            launchSingleTop = true
                        }}
                    )
                    NavigationBarItem(
                    icon = {
                        Icon(
                            tint = LightBlue400,
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(

                            "Profile"
                        )
                    },
                    selected = true,
                    onClick = {}
                )

            } }
        ) { padding ->
            Profile(navController, Modifier.padding(padding), authRepository)
        }
    }
}

@Composable
fun Profile(navController: NavController, modifier: Modifier = Modifier, authRepository: AuthRepository) {
    //variable for storing the fetched quote
    var quotes by remember { mutableStateOf<List<Quote>>(emptyList()) }
    //variable for checking the load state of the API
    var isLoading by remember { mutableStateOf(false) }

    //Getting the quote from zenquotes API
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val response = RetrofitInstance.api.getRandomQuote()
            quotes = response // storing the response from the API
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    //Column for displaying the quote from zenquotes
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val user = Firebase.auth.currentUser
        if (isLoading) {
            CircularProgressIndicator(
                color = LightBlue300
            )
        } else {
            if (quotes.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(quotes) { quote ->
                        QuoteCard(quote)
                    }
                }
            } else {
                Text("Failed to load quotes.")
            }
        }
        //text for displaying the current user email
        user?.let {
            Text(
                text = "User: ${it.email}",
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        //button for logging out the current user
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = LightBlue600,
                contentColor = LightBlue50
            ),
            onClick = {
                authRepository.logout()
                //navigation for going to the login screen
                navController.navigate(AppRoutes.Login.route) {
                    popUpTo(AppRoutes.Home.route) { inclusive = true }
                }
            }

        ) {
            Text("Logout")
        }
    }
}

//quote card for the quote from zenquotes
@Composable
fun QuoteCard(quote: Quote) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //displaying the quote
            Text(
                text = "\"${quote.q}\"",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            //displaying the quote author
            Text(
                text = "- ${quote.a}",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

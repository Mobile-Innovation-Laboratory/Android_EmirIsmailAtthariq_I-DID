package com.motion.i_did.ui.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.motion.i_did.R
import com.motion.i_did.core.routes.AppRoutes
import com.motion.i_did.data.Note
import com.motion.i_did.repository.AuthRepository
import com.motion.i_did.repository.NotesRepository
import com.motion.i_did.ui.Components.Note.NoteItem
import com.motion.i_did.ui.theme.LightBlue300
import com.motion.i_did.ui.theme.LightBlue400
import com.motion.i_did.ui.theme.LightBlue600

//Home Screen of the app
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController, authRepository: AuthRepository
){
    //declaring variables to stores the notes
    var notes by remember { mutableStateOf(listOf<Note>()) }
    //declaring variables to stores the error message
    var errorMessage by remember { mutableStateOf("") }
    //declaring variables to stores the current user ID
    val userId = authRepository.getCurrentUserId() ?: return
    //declaring variables to get the notes Repository
    val notesRepository = remember { NotesRepository(userId) }


    //Fetching current user Notes
    LaunchedEffect(Unit) {
        notesRepository.getNotesRealTime().collect{ results ->
            results.onSuccess {
                 notes = it
                }.onFailure{
                 errorMessage = it.localizedMessage ?: "error"
            }
        }
    }

        //top bar of the home screen
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        // title bar
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.idid),
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .size(250.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                        }
                    },

                )
            },


                    //bottom bar of the home screen
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ) { NavigationBarItem(
                    icon = {
                        Icon(
                            tint = LightBlue400,
                            imageVector = Icons.Default.Home,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            "Home"
                        )
                    },
                    selected = true,
                    onClick = {

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
                            navController.navigate(AppRoutes.Favorite.route)
                            {
                            launchSingleTop = true
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(

                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(
                                "Profile"
                            )
                        },
                        selected = false,
                        onClick = {
                            //navigation for going to the  Profile screen
                            navController.navigate(AppRoutes.Profile.route)
                            {
                            launchSingleTop = true
                            }
                        }
                    )
                } }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // New note input
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        color = LightBlue600,
                        modifier = Modifier
                            .weight(9f)
                            .padding(16.dp),
                        text = "What did you do?",
                    )
                    IconButton(

                        modifier = Modifier.weight(1f).size(24.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            contentColor = LightBlue300

                        ),
                        onClick = {
                            //navigation for going to the input note screen
                            navController.navigate(AppRoutes.NoteScreen.route) {
                                launchSingleTop = true
                            }
                        }
                    ) {
                        Icon(

                            Icons.AutoMirrored.Filled.Send, contentDescription = "Add Note"
                        )
                    }

                }

                // Error Message for the home screen
                if (errorMessage.isNotBlank()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                // Notes List
                LazyColumn {
                    // Load all notes
                    items(notes) { note ->
                        NoteItem(
                            note = note,
                            onNoteClick = {
                                // navigation for going to the detailed note screen
                                navController.navigate(AppRoutes.MoreNoteScreen.createRoute(note.title)) {
                                    popUpTo(AppRoutes.Home.route)
                                }
                            }
                        )
                    }
                }
            }
        }
}




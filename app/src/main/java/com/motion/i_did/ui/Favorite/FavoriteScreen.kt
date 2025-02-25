package com.motion.i_did.ui.Favorite

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.motion.i_did.core.routes.AppRoutes
import com.motion.i_did.data.Note
import com.motion.i_did.data.local.database.FavoriteDatabase
import com.motion.i_did.data.local.repository.FavoriteRepository
import com.motion.i_did.repository.AuthRepository
import com.motion.i_did.repository.NotesRepository
import com.motion.i_did.ui.Components.Note.NoteItem
import com.motion.i_did.ui.theme.IDIDTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//Favorite Screen for the notes on the app
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    navController: NavController, authRepository: AuthRepository
){
    //declaring variables to stores the notes
    var notes by remember { mutableStateOf(listOf<Note>()) }
    //declaring variables to stores the error message
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
    //declaring variables to stores the current user id
    val userId = authRepository.getCurrentUserId() ?: return
    //declaring variables to stores the notes repository of current user
    val notesRepository = remember { NotesRepository(userId) }
    //declaring variables to stores the favorite database
    val database = remember { FavoriteDatabase.getDatabase(context) }
    //declaring variables to get the favorite repository
    val favoriteRepository = remember { FavoriteRepository.getInstance(database.favoriteDao()) }
    //declaring variables to stores the favorites repository
    val favorites by favoriteRepository.getFavorites().observeAsState(emptyList())

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

    IDIDTheme {
        Scaffold(
            // top bar of the Favorite Screen
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            modifier = Modifier
                                .padding(top = 4.dp)
                            ,text ="Favorites",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary)},
                    actions = {
                        IconButton(
                            onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    favoriteRepository.deleteALL()
                                }
                                Toast.makeText(
                                    context,
                                    "Favorites Deleted",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                )

            },
            //bottom bar of the favorite screen
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
                        selected = true,
                        onClick = {}
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
                            //navigation for going to the Profile Screen
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

                // Notes List for the favorite notes
                LazyColumn {
                    items(favorites) { favorite ->
                        NoteItem(
                            note = Note(
                                id = favorite.id,
                                title = favorite.title,

                            ),
                            onNoteClick = {
                                //navigation for going to the detailed note screen
                                navController.navigate(AppRoutes.MoreNoteScreen.createRoute(favorite.title)) {
                                    popUpTo(AppRoutes.Home.route)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

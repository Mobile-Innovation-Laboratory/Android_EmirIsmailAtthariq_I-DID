package com.motion.i_did.ui.Note

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.motion.i_did.core.routes.AppRoutes
import com.motion.i_did.data.Note
import com.motion.i_did.data.local.database.FavoriteDatabase
import com.motion.i_did.data.local.entity.FavoriteEntity
import com.motion.i_did.data.local.repository.FavoriteRepository
import com.motion.i_did.repository.AuthRepository
import com.motion.i_did.repository.NotesRepository
import kotlinx.coroutines.launch

//Detailed Note screen for the app
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreNoteScreen(
    navController: NavController,
    authRepository: AuthRepository,
    noteTitle: String?,
){
    //variable for storing the note
    var notes by remember { mutableStateOf(listOf<Note>()) }
    //variable for storing the error message
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
    //value for storing the current user ID
    val userId = authRepository.getCurrentUserId() ?: return
    //value for storing the notes repository of the current user
    val notesRepository = remember { NotesRepository(userId) }
    val scope = rememberCoroutineScope()
    //value to stores the note that are going to be viewe
    val note = notes.find{it.title == noteTitle}
    //values for the favorite database and favorite Repository
    val database = remember { FavoriteDatabase.getDatabase(context) }
    val favoriteRepository = remember { FavoriteRepository.getInstance(database.favoriteDao()) }


    //Fetching the note
    LaunchedEffect(Unit) {
        notesRepository.getNotesRealTime().collect{ results ->
            results.onSuccess {
                notes = it
            }.onFailure{
                errorMessage = it.localizedMessage ?: "error"
            }
        }
    }

    Scaffold(
        //top bar of the Detailed note screen
        topBar = {
            TopAppBar(
                title = {
                    //loads the note title
                    Text(
                        modifier = Modifier
                            .padding(top = 4.dp)
                        ,text = note?.title ?: "Title not found",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary)},

                actions = {
                    IconButton(
                        onClick = {
                            //navigation for going to the home screen
                            navController.navigate(AppRoutes.Home.route) {
                                popUpTo(AppRoutes.Home.route) { inclusive = true }
                            }
                        }
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Back")
                    }
                }
            )

        },
        //bottom bar for the detailed note screen
        bottomBar = {
            NavigationBar {
                //button for deleting note
                IconButton(
                    modifier = Modifier.padding(start = 24.dp),
                    onClick = {
                        scope.launch {
                            val result = notesRepository.deleteNote(note?.id)
                            result.onFailure {
                                errorMessage = it.localizedMessage ?: "Failed to delete note"
                            }
                            Toast.makeText(
                                context,
                                "Note Deleted",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        note?.let {
                            val favorite = FavoriteEntity(
                                id = it.id ?: "",
                                title = it.title,
                            )
                            scope.launch {
                                favoriteRepository.deleteFavorite(favorite)
                            }
                        }
                        //navigation for going to the home route
                        navController.navigate(AppRoutes.Home.route)
                    }
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
                //button for favoriting the note
                IconButton(
                    modifier = Modifier.padding(start = 24.dp),
                    onClick = {
                        note?.let {
                            val favorite = FavoriteEntity(
                                id = it.id ?: "",
                                title = it.title,
                            )
                            scope.launch {
                                val isFavorite =
                                    favoriteRepository.isFavorite(favorite.id) > 0 // checking if already favorite

                                if (isFavorite) {
                                    favoriteRepository.deleteFavorite(favorite) // Deleting if already favorite
                                    Toast.makeText(
                                        context,
                                        "Deleted from Favorites",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    favoriteRepository.insertFavorite(favorite) // Adding to favorite if aren't already
                                    Toast.makeText(
                                        context,
                                        "Added to Favorites",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }


                    }
                ) {
                    Icon(Icons.Default.Star, contentDescription = "Favorite")
                }
                //Button for editing the note
                IconButton(
                    modifier = Modifier.padding(start = 24.dp),
                    onClick = {
                        if (note != null) {
                            //navigation for going to the edit note screen
                            navController.navigate(AppRoutes.EditNoteScreen.createRoute(note.title)) {
                                popUpTo(AppRoutes.Home.route)
                            }
                        }
                    }
                ) {
                    Icon(Icons.Default.Create, contentDescription = "Edit")
                }

            }
        },
    ){padding->
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .padding(16.dp)

        ) {
            //loads the note content
            Text(
                modifier = Modifier,
                text = note?.content ?: "content not found")

        }
    }
}
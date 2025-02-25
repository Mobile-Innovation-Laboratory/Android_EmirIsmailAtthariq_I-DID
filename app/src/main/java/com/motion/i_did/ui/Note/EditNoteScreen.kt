package com.motion.i_did.ui.Note

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.motion.i_did.core.routes.AppRoutes
import com.motion.i_did.data.Note
import com.motion.i_did.repository.AuthRepository
import com.motion.i_did.repository.NotesRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Date
//Edit Note Screen for the app
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    navController: NavController,
    authRepository: AuthRepository,
    noteTitle: String?,
) {
    //values for storing the current User ID
    val userId = authRepository.getCurrentUserId() ?: return
    //values for storing the notes Repository
    val notesRepository = remember { NotesRepository(userId) }
    val scope = rememberCoroutineScope()

    //variable for storing the note
    var note by remember { mutableStateOf<Note?>(null) }
    //variable for storing the error message
    var errorMessage by remember { mutableStateOf("") }
    //variable for storing the edited note title
    var newNoteTitle by remember { mutableStateOf("") }
    //variable for storing the edited note content
    var newNoteContent by remember { mutableStateOf("") }

    //fetching the note that are going to be edited
    LaunchedEffect(noteTitle) {
        scope.launch {
            notesRepository.getNotesRealTime().collectLatest { result ->
                result.onSuccess { notesList ->
                    val fetchedNote = notesList.find { it.title == noteTitle }
                    if (fetchedNote != null) {
                        note = fetchedNote
                        newNoteTitle = fetchedNote.title
                        newNoteContent = fetchedNote.content
                    } else {
                        errorMessage = "Note not found"
                    }
                }.onFailure {
                    errorMessage = it.localizedMessage ?: "Failed to fetch notes"
                }
            }
        }
    }

    Scaffold(
        //top  bar for the edit note screen
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "Edit Note",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    //text field for changing the note title
                    value = newNoteTitle,
                    onValueChange = { newNoteTitle = it },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    placeholder = { Text("Title") },
                    maxLines = 1
                )
                TextField(
                    //text field for changing the note content
                    value = newNoteContent,
                    onValueChange = { newNoteContent = it },
                    modifier = Modifier.fillMaxWidth().weight(9f).padding(16.dp),
                    placeholder = { Text("Content") },
                    maxLines = 30
                )
                IconButton(
                    modifier = Modifier.weight(1f).size(24.dp),
                    onClick = {
                        //Updating the note title and note content
                        if (newNoteTitle.isNotBlank() && newNoteContent.isNotBlank()) {
                            scope.launch {
                                val updatedNote = note?.copy(
                                    title = newNoteTitle,
                                    content = newNoteContent,
                                    createdAt = Date()
                                )
                                if (updatedNote != null) {
                                    val result = notesRepository.updateNote(updatedNote)
                                    result.onFailure {
                                        errorMessage = it.localizedMessage ?: "Failed to update note"
                                    }
                                }
                            }
                            //navigation for going to the home screen
                            navController.navigate(AppRoutes.Home.route) {
                                popUpTo(AppRoutes.Home.route) { inclusive = true }
                            }
                        }
                    }
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Update Note")
                }
            }
            //error message for the edit note screen
            if (errorMessage.isNotBlank()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

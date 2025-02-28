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
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.motion.i_did.ui.theme.LightBlue300
import com.motion.i_did.ui.theme.LightBlue500
import com.motion.i_did.ui.theme.LightBlue600
import kotlinx.coroutines.launch
import java.util.Date

//Adding a note screen for the app
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(
    navController: NavController,
    authRepository: AuthRepository
) {
    //variable for storing the new note title
    var newNotetitle by remember { mutableStateOf("") }
    //variable for storing the new note content
    var newNoteContent by remember { mutableStateOf("") }
    //variable for storing the error message
    var errorMessage by remember { mutableStateOf("") }
    //value for storing the current user ID
    val userId = authRepository.getCurrentUserId() ?: return
    //value for storing the current user notes repository
    val notesRepository = remember { NotesRepository(userId) }
    val scope = rememberCoroutineScope()
    Scaffold(
        //top bar of the adding a note screen
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier
                            .padding(top = 4.dp), text = "What did you do?",
                        style = MaterialTheme.typography.titleLarge,
                        color = LightBlue600,
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
            //column for inputting a new note
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    colors = TextFieldDefaults.colors(
                        focusedLabelColor = LightBlue300,
                        focusedIndicatorColor = LightBlue300,
                        unfocusedIndicatorColor = LightBlue500
                    ),
                    //text field for the new note title
                    value = newNotetitle,
                    onValueChange = { newNotetitle = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("What happened?") },
                    maxLines = 1

                )
                TextField(
                    colors = TextFieldDefaults.colors(
                        focusedLabelColor = LightBlue300,
                        focusedIndicatorColor = LightBlue300,
                        unfocusedIndicatorColor = LightBlue500
                    ),
                    //text field for the new note content
                    value = newNoteContent,
                    onValueChange = { newNoteContent = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(9f)
                        .padding(16.dp),
                    placeholder = { Text("How it happened?") },
                    maxLines = 30
                )
                //button for adding the new note
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = LightBlue300

                    ),
                    modifier = Modifier.weight(1f).size(24.dp),
                    onClick = {
                        if (newNoteContent.isNotBlank()) {
                            scope.launch {
                                val newNote = Note(
                                    title = newNotetitle,
                                    content = newNoteContent,
                                    userId = userId,
                                    createdAt = Date()
                                )
                                val result = notesRepository.createNote(newNote)
                                result.onFailure {
                                    errorMessage = it.localizedMessage ?: "Failed to Create note"
                                }
                                newNoteContent = ""
                                newNotetitle = ""
                            }
                            //navigation for going to the home screen
                            navController.navigate(AppRoutes.Home.route){
                                popUpTo(AppRoutes.Home.route){ inclusive = true }
                            }
                        }
                    }
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Add Note")
                }
            }
            //Error message for the adding a new note screen
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


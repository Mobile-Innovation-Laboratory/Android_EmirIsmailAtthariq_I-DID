package com.motion.i_did.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import com.motion.i_did.data.Note
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await


class  NotesRepository(private val userId: String) {

    //Connecting to firestore
    private val firestore = FirebaseFirestore.getInstance()

    private val notesRef = firestore.collection("notes")

    // Create a new note
    suspend fun createNote(note: Note): Result<String> {
        return try {

            val newNote = note.copy(userId = userId)
            val resultNote = notesRef.add(newNote).await()

            Result.success(resultNote.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    //get notes for the current user
    fun getNotesRealTime(): Flow<Result<List<Note>>> = callbackFlow {

        val listenerRegistration: ListenerRegistration = notesRef
            .whereEqualTo("userId", userId)
            .orderBy("createdAt")
            .addSnapshotListener{ snapshot, e ->
                if(e!=null){
                    Log.e("NotesRepository", "getNotesRealTime: " + e.localizedMessage)
                    trySend(Result.failure(e))
                    return@addSnapshotListener
                }
                if(snapshot != null){
                    val notes = snapshot.documents.mapNotNull {
                        it.toObject<Note>()?.copy(id = it.id)
                    }
                    trySend(Result.success(notes))

                }
            }
        awaitClose {

            listenerRegistration.remove()
        }
    }

    // Delete a note
    suspend fun deleteNote(noteId: String?): Result<Unit> {
        return try {

            noteId?.let { notesRef.document(it).delete().await() }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Update an existing note
    suspend fun updateNote(note: Note): Result<Unit> {
        return try {

            notesRef.document(note.id).set(note).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

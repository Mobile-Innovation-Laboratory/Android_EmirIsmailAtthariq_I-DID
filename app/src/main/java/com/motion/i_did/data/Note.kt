package com.motion.i_did.data

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date
//Data class for the note
data class Note(
    //stores the information of a created note
    @DocumentId
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val userId: String = "",
    @ServerTimestamp
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
)


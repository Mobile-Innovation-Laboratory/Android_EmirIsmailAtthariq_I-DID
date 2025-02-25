package com.motion.i_did.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository(private val context: Context) {
    //Repository for Authentication using firebase
    private val auth = FirebaseAuth.getInstance()

    // Email and Password Authentication
    suspend fun signInWithEmailPassword(email: String, password: String): Result<String> {
        return try {

            val resultAuth = auth.signInWithEmailAndPassword(email,password).await()

            Result.success(resultAuth.user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Email and Password Registration
    suspend fun registerWithEmailPassword(email: String, password: String): Result<String> {
        return try {

            val resultAuth = auth.createUserWithEmailAndPassword(email, password).await()

            Result.success(resultAuth.user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    // Check if user is currently logged in
    fun isUserLoggedIn(): Boolean = auth.currentUser != null



    // Get current user ID
    fun getCurrentUserId(): String? = auth.currentUser?.uid

    // Logout
    fun logout() {

        auth.signOut()
    }
}
package com.motion.i_did.core.routes
//Routes for app navigation
sealed class AppRoutes(val route: String) {
    // Authentication Routes
    object Splash : AppRoutes("splash")
    object Login : AppRoutes("login")
    object Register: AppRoutes("register")
    object Favorite: AppRoutes("favorite")
    // Main App Routes
    object Home : AppRoutes("home")
    object Profile : AppRoutes("profile")
    object NoteScreen : AppRoutes("notescreen")
    object MoreNoteScreen : AppRoutes("morenotescreen/{noteTitle}") {
        fun createRoute(noteTitle: String): String = "morenotescreen/$noteTitle"
    }
    object EditNoteScreen : AppRoutes("editnotescreen/{noteTitle}"){
        fun createRoute(noteTitle: String): String = "editnotescreen/$noteTitle"
    }
}
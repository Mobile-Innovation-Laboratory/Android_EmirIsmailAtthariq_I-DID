package com.motion.i_did

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.motion.i_did.core.routes.AppRoutes
import com.motion.i_did.repository.AuthRepository
import com.motion.i_did.ui.Favorite.FavoriteScreen
import com.motion.i_did.ui.Home.HomeScreen
import com.motion.i_did.ui.Note.EditNoteScreen
import com.motion.i_did.ui.Note.MoreNoteScreen
import com.motion.i_did.ui.Note.NoteScreen
import com.motion.i_did.ui.Profile.ProfileScreen
import com.motion.i_did.ui.login.LoginScreen
import com.motion.i_did.ui.register.RegisterScreen
import com.motion.i_did.ui.splash.SplashScreen
// navigation for the app
@Composable
fun AppNavigation(){
    val context = LocalContext.current
    val navController = rememberNavController()
    val authRepository = remember { AuthRepository(context) }
    NavHost(
        navController = navController,
        startDestination = AppRoutes.Splash.route
    ){
        //Splash
        composable(AppRoutes.Splash.route) {
            SplashScreen(navController, authRepository)
        }

        //Login
        composable(AppRoutes.Login.route){
            LoginScreen(navController, authRepository)
        }

        //Home
        composable(AppRoutes.Home.route){
            HomeScreen(navController, authRepository)
        }
        //Profile
        composable(AppRoutes.Profile.route) {
            ProfileScreen(navController, authRepository)
        }
        //Favorite
        composable(AppRoutes.Favorite.route){
            FavoriteScreen(navController,authRepository)
        }
        //Register
        composable(AppRoutes.Register.route){
            RegisterScreen(navController, authRepository)
        }
        //NoteScreen
        composable(AppRoutes.NoteScreen.route){
            NoteScreen(navController, authRepository)
        }
        //EditNoteScreen
        composable(AppRoutes.EditNoteScreen.route,
            arguments = listOf(navArgument("noteTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            val noteTitle = backStackEntry.arguments?.getString("noteTitle")
            EditNoteScreen(navController, authRepository, noteTitle)
        }
        //MoreNoteScreen
        composable(
            route = AppRoutes.MoreNoteScreen.route,
            arguments = listOf(navArgument("noteTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            val noteTitle = backStackEntry.arguments?.getString("noteTitle")
            MoreNoteScreen(navController, authRepository, noteTitle)
        }

    }
}
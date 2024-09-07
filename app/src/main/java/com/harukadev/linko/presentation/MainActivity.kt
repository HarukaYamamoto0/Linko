package com.harukadev.linko.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.harukadev.linko.presentation.screens.HomeScreen
import com.harukadev.linko.presentation.screens.ResultScreen
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = HomeRouter) {
                composable<HomeRouter> { HomeScreen(navController) }
                composable<ResultRouter> {
                    val args = it.toRoute<ResultRouter>()
                    ResultScreen(navController, url = args.shortenedUrl)
                }
            }
        }
    }
}

@Serializable
object HomeRouter

@Serializable
data class ResultRouter(val shortenedUrl: String)
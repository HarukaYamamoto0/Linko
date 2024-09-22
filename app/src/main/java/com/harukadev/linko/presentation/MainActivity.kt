package com.harukadev.linko.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.harukadev.linko.presentation.screens.home.HomeRouter
import com.harukadev.linko.presentation.screens.home.HomeScreen
import com.harukadev.linko.presentation.screens.result.ResultRouter
import com.harukadev.linko.presentation.screens.result.ResultScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = HomeRouter) {
                composable<HomeRouter> {
                    HomeScreen(navController = navController)
                }
                composable<ResultRouter> {
                    val args = it.toRoute<ResultRouter>()
                    ResultScreen(navController, args = args)
                }
            }
        }
    }
}
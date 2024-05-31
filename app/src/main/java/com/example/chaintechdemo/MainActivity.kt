package com.example.chaintechdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chaintechdemo.navigation.Screens
import com.example.chaintechdemo.screens.DashboardScreen
import com.example.chaintechdemo.ui.theme.ChainTechDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChainTechDemoTheme {
                AppMain()
            }
        }
    }
}

@Composable
fun AppMain() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.DashboardScreen.route) {
        composable(route = Screens.DashboardScreen.route) {
            DashboardScreen()
        }
    }
}

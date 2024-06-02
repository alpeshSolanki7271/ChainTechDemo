package com.example.chaintechdemo.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import com.example.chaintechdemo.navigation.Screens
import com.example.chaintechdemo.util.BiometricHelper

@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val activity = LocalContext.current as FragmentActivity
    val biometricHelper = remember { BiometricHelper(activity) }

    if (biometricHelper.isBiometricAvailable()) {
        biometricHelper.showBiometricPrompt(activity, onSuccess = {
            navController.navigate("dashboard") {
                popUpTo(Screens.HomeScreen.route) {
                    inclusive = true
                }
            }
        }, onError = { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        })
    } else if (biometricHelper.isDeviceCredentialAvailable()) {
        biometricHelper.showDeviceCredentialPrompt(activity, onSuccess = {
            navController.navigate("dashboard") {
                popUpTo(Screens.HomeScreen.route) {
                    inclusive = true
                }
            }
        }, onError = { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        })
    } else {
        navController.navigate("dashboard") {
            popUpTo(Screens.HomeScreen.route) {
                inclusive = true
            }
        }
    }
}
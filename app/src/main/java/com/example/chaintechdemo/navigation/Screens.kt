package com.example.chaintechdemo.navigation

sealed class Screens(val route : String){

    object DashboardScreen : Screens("dashboard")



    fun withArgs(vararg args: Any): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}



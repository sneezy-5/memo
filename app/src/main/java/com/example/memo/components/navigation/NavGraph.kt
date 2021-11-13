package com.example.memo.components.navigation



import androidx.navigation.NavController
import com.example.memo.components.navigation.Destinations.AddTodoView

//import androidx.navigation.compose.navigate


class NavGraph(navController: NavController) {
    val addTodo: () -> Unit = {
        navController.navigate(Destinations.Home)
    }

}

object Destinations {
    const val Home = "home"
    const val AddTodoView = "addTodoView"
    const val AddTodoUpdate = "addTodo/{todo}"
    const val Details = "DetailsView/{todo}"
}
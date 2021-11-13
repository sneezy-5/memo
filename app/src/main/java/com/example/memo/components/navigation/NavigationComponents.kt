package com.example.memo.components.navigation


import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.ui.navigateUp
import com.example.memo.HomeView
import com.example.memo.components.AddMemo.AddTodoView
import com.example.memo.components.updateMemo.AddView
import com.example.memo.components.updateMemo.DetailPage
//import com.example.memo.components.AddMemo.DetailPage


import com.example.memo.database.TodoItem
import com.example.memo.ui.theme.MemoTheme
import com.google.gson.Gson



@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun NavigationComponent() {
    val navController = rememberNavController()

    MemoTheme {
        NavHost(navController = navController, startDestination = Destinations.Home) {

            composable(Destinations.Home) { HomeView(navController, navigateUp = { navController.popBackStack() }) }
            composable(Destinations.AddTodoView) { AddTodoView(navController, navigateUp = { navController.popBackStack(Destinations.AddTodoView, inclusive = true) }) }
           composable(Destinations.AddTodoUpdate)  { backStackEntry ->
                backStackEntry?.arguments?.getString("todo")?.let { json ->
                    val user = Gson().fromJson(json, TodoItem::class.java)
                    AddView(navController,todo = user, navigateUp={ navController.popBackStack()})
                }
            }
            composable(Destinations.Details, arguments = listOf(
                    navArgument("todo") { type = NavType.StringType }
                    )
            ) { backStackEntry ->
                backStackEntry?.arguments?.getString("todo")?.let { json ->
                    val user = Gson().fromJson(json, TodoItem::class.java)
                    DetailPage(navController,todo = user, navigateUp={ navController.popBackStack(Destinations.Details, inclusive = true) })
                }
            }

/*composable(
                "userDetailsView/{todo}",
                arguments = listOf(
                    navArgument("todo") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                backStackEntry?.arguments?.getString("todo")?.let { json ->
                    val user = Gson().fromJson(json, TodoItem::class.java)
                    DetailPage(navController,todo = user)
                }
            }*/

        }
    }
}


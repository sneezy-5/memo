package com.example.memo.components.updateMemo


import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.memo.DialogDemo
import com.example.memo.components.navigation.Destinations
import com.example.memo.database.TodoViewModel
import com.example.memo.database.TodoViewModelFactory
import com.example.memo.R
import com.example.memo.database.TodoItem
import com.google.gson.Gson

@ExperimentalMaterialApi
@Composable
fun DetailPage(navController: NavController, todo:TodoItem, navigateUp: () -> Unit){
    val context = LocalContext.current
    val mTodoViewModel: TodoViewModel = viewModel(
        factory = TodoViewModelFactory(context.applicationContext as Application)
    )
    val (showDialog, setShowDialog) =  remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppTopBarD(onClick = navigateUp, onClickAdd = {  setShowDialog(true) })
        },

        ) {
        DialogDemo(showDialog, setShowDialog, todo, mTodoViewModel, navigateUp)
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable(enabled = true, onClick = {
                val userJson = Gson().toJson(todo)
                navController.navigate("addTodo/$userJson")

            })) {
            Column() {
                ListItem(
                    text = { Text(text = todo.itemName, fontSize =20.sp) },
                    //modifier = Modifier.background(Color.LightGray)
                    secondaryText = { Text(text = todo.itemText, fontSize = 18.sp)},
                    modifier = Modifier.padding(12.dp)

                    )
                Spacer(modifier = Modifier.padding(10.dp)) // space elemets
                Text(text = todo.dateTime.toString(), fontSize = 12.sp, modifier = Modifier.padding(12.dp))
            }


        }
    }
}


@Composable
fun AppTopBarD(onClick: () -> Unit, onClickAdd: () -> Unit) {
    TopAppBar(
        title = { Text("Detail Memo", color = Color.White) },
        elevation = 12.dp,
        backgroundColor = Color.Red,
        actions = {
            IconButton(onClick = onClickAdd) {
                Icon( Icons.Default.Delete, contentDescription = "delete")
                //Text(text = "SAVE")
            }
        },
        navigationIcon = {

            IconButton(onClick = onClick) {
                Icon( painter = painterResource(id = R.drawable.ic_baseline_west_24), contentDescription = "e")
                //Text(text = "SAVE")
            }
        }

    )
}


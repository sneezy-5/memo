package com.example.memo


import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_BACK
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
//import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.memo.components.AddMemo.InputViewModel
//import com.example.memo.components.AddMemo.InputViewModel
//import androidx.navigation.compose.navigate
import com.example.memo.components.navigation.Destinations
import com.example.memo.components.navigation.NavigationComponent
import com.example.memo.database.TodoItem
import com.example.memo.database.TodoViewModel
import com.example.memo.database.TodoViewModelFactory
import com.example.memo.ui.theme.MemoTheme
import com.google.gson.Gson


class MainActivity : ComponentActivity() {


    @ExperimentalMaterialApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            MemoTheme {
                Surface(color = MaterialTheme.colors.background){
                    NavigationComponent()

                }
            }



        }

    }

   override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        return if (keyCode == KEYCODE_BACK ) {
            //Log.i("=BACK BUTTON=", "BACK BUTTON $keyCode")
            //Toast.makeText(applicationContext,  R.string.quitter, Toast.LENGTH_SHORT).show()
            this.finish()

            true


        } else {
            Log.i("=ELSE BACK BUTTON =", "ELSE BACK BUTTON")
            super.onKeyDown(keyCode, event)
        }
    }
}




@ExperimentalFoundationApi
@Composable
fun HomeView(navController: NavController,  navigateUp: () -> Unit) {
    val context = LocalContext.current
    val mTodoViewModel: TodoViewModel = viewModel(
        factory = TodoViewModelFactory(context.applicationContext as Application)
    )
    //val state = InputViewModel()
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    val searchState = InputViewModel()
    val items = mTodoViewModel.readAllData.observeAsState(listOf()).value
    Scaffold(topBar = {
            AppTopBar(searchState){
                //mTodoViewModel.deleteTodo()
            }
    },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                    navController.navigate(Destinations.AddTodoView)

                },
                backgroundColor = colorResource(id = R.color.search_color),
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_add_24),
                        contentDescription = null,
                        tint = Color.White
                    )
                 }
            )
        }) {
        //SearchView(inputViewModel = state)
        Column {
            SearchView(state = textState)
            Spacer(modifier = Modifier.padding(10.dp))
            TodoList(list = items , mTodoViewModel = mTodoViewModel, navController = navController, textState)

        }

    }

}


@ExperimentalFoundationApi
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TodoList(
    list: List<TodoItem>,
    mTodoViewModel: TodoViewModel,
    navController: NavController,
    //state: InputViewModel,
    state: MutableState<TextFieldValue>
) {
    //val context = LocalContext.current
    val (showDialog, setShowDialog) =  remember { mutableStateOf(false) }
    fun navigateToUser(todo: TodoItem) {
        val userJson = Gson().toJson(todo)
        navController.navigate("DetailsView/$userJson")


    }

    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        val searchedText = state.value.text
        if (searchedText.isEmpty()){

            items(list) { todo ->
                //val name = rememberSaveable { mutableStateOf(todo.isDone) }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.Unspecified)
                        .padding(5.dp)
                        .fillMaxSize()
                        .clickable(enabled = true, onClick = { navigateToUser(todo) })
                ) {

                    ListItem(
                        text = { Text(text = todo.itemName,
                            fontSize= 20.sp,
                            fontWeight = FontWeight(1),
                            fontFamily = FontFamily.Monospace,
                            color=Color.DarkGray)
                        },
                        secondaryText = { Text(text = todo.itemText, Modifier.height(50.dp)) },
                        icon = {
                            IconButton(onClick = {
                                setShowDialog(true)
                                mTodoViewModel.deleteTodo(todo)
                            }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = null
                                )
                            }
                        },
                    )
                }

                Divider()
            }

        } else {
            for (its in list){
                if (searchedText in its.itemName){
                    Log.i("verric", "isit")
                    item { //filteredCountry ->
                        //val name = rememberSaveable { mutableStateOf(todo.isDone) }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = Color.Unspecified)
                                .padding(5.dp)
                                .fillMaxSize()
                                .clickable(enabled = true, onClick = { navigateToUser(its) })
                        ) {

                            ListItem(
                                text = { Text(text = its.itemName,
                                    fontSize= 18.sp,
                                    fontWeight = FontWeight(1),
                                    fontFamily = FontFamily.Monospace,
                                    color=Color.DarkGray)
                                },
                                //modifier = Modifier.background(Color.LightGray)
                                secondaryText = { Text(text = its.itemText, Modifier.height(50.dp)) },
                                icon = {
                                    IconButton(onClick = {
                                        setShowDialog(true)
                                        mTodoViewModel.deleteTodo(its)
                                    }) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = null
                                        )
                                    }
                                },
                                //modifier = Modifier.height(50.dp).padding(50.dp)
                            )
                        }
                    }
                }
            }

        }

    }

    // Create alert dialog, pass the showDialog state to this Composable
    //DialogDemo(showDialog, setShowDialog,, )
}

@Composable
fun DialogDemo(showDialog: Boolean, setShowDialog: (Boolean) -> Unit, todo:TodoItem, mTodoViewModel: TodoViewModel, navigateUp: () -> Unit) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
            },
            title = {
                Text(stringResource(id = R.string.app_pop_r))
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                    onClick = {
                        // Change the state to close the dialog
                        setShowDialog(false)
                        mTodoViewModel.deleteTodo(todo)
                        navigateUp()

                    },
                ) {
                    Text(stringResource(id = R.string.app_delete), color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Yellow),
                    onClick = {
                        // Change the state to close the dialog
                        setShowDialog(false)
                    },
                ) {
                    Text(stringResource(id = R.string.app_annuler), color = Color.White)
                }
            },
            text = {
                Text(stringResource(id = R.string.app_popup))
            },
        )
    }
}

@Composable
fun AppTopBar( inputViewModel: InputViewModel, onClick: () -> Unit) {
    TopAppBar(
        title = { Text("Memo", color = Color.White) },
        elevation = 12.dp,
        backgroundColor = Color.Red,
        actions = {

            //SearchView(inputViewModel)

        },

/*navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = null
                )
            }
        }*/


    )
}










@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MemoTheme {
        //HomeView(navController = )
    }
}

class InputViewModel: ViewModel() {

    private val _search: MutableLiveData<String> = MutableLiveData("")
    val todo: LiveData<String> = _search

    fun onInputChange(newName: String) {
        _search.value = newName
    }


}





@Composable
fun SearchView(state: MutableState<TextFieldValue>) {
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        modifier = Modifier
            .fillMaxWidth(),
        textStyle = TextStyle(color = Color.White, fontSize = 18.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (state.value != TextFieldValue("")) {
                IconButton(
                    onClick = {
                        state.value =
                            TextFieldValue("") // Remove text from TextField when you press the 'X' icon
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.White,
            cursorColor = Color.White,
            leadingIconColor = Color.White,
            trailingIconColor = Color.White,
            backgroundColor = colorResource(id = R.color.search_color),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

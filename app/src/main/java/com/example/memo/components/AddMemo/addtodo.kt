package com.example.memo.components.AddMemo

import android.app.Application
import android.view.KeyEvent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape

import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.memo.R
import com.example.memo.database.TodoItem
import com.example.memo.database.TodoViewModel
import com.example.memo.database.TodoViewModelFactory
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun AddTodoView(navController: NavController, model: InputViewModel = viewModel(), navigateUp: () -> Unit) { // 2.
    val count by model.todo.observeAsState("") // 3.
    val text: String by model.text.observeAsState( initial = "")
    //val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val mTodoViewModel: TodoViewModel = viewModel(
        factory = TodoViewModelFactory(context.applicationContext as Application)
    )


    fun onclik(){
         if (model.todo.value!= null) {
             insertTodoInDB(
                 model.todo.value.toString(),
                 model.text.value.toString(),
                 mTodoViewModel
             )
         }
    }

    Scaffold(
        topBar = {
         AppTopBar(onClick = {
             navigateUp()
         }, onClickAdd = {
             onclik()
             navigateUp()
         })
        }
 ) {
        Column(Modifier.fillMaxSize()) {

            val focusRequester = FocusRequester()

            TextField(
                value = count,
                onValueChange = {
                    model.onInputChange(it)
                    //text = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    //.focusRequester(focusRequester = focusRequester)
                    .onKeyEvent {
                        if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                            focusRequester.requestFocus()
                            true
                        }
                        false
                    },
                textStyle = TextStyle(color = Color.Black, fontSize = 25.sp),
                placeholder = { Text(text = "Enter title") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {focusRequester.requestFocus()}),
                singleLine = true,
                shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    cursorColor = Color.Magenta,
                    leadingIconColor = Color.White,
                    trailingIconColor = Color.White,
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

            DisposableEffect(Unit) {
                focusRequester.requestFocus()
                onDispose { }
            }

            TextField(
                value = text,
                onValueChange = {
                    model.onInputChang(it)
                    //text = it
                },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .focusRequester(focusRequester),
                placeholder = { Text(text = "Enter text...") },
                singleLine = false,
                shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black,
                    cursorColor = Color.Magenta,
                    leadingIconColor = Color.White,
                    trailingIconColor = Color.White,
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )

        }

    }

}




fun insertTodoInDB(todo: String, todotext:String, mTodoViewModel: TodoViewModel) {

    if (todotext.isNotEmpty()) {
        var todoItem:TodoItem
        if (todo.isEmpty()){
             todoItem = TodoItem(
                itemName = "",
                itemText = todotext,
                 //datTime = LocalDateTime.now()
                dateTime = Date()
            )

        }else{
             todoItem = TodoItem(
                itemName = todo,
                //datTime = LocalDateTime.now(),
                itemText = todotext,
                 dateTime = Date()
            )
        }


        mTodoViewModel.addTodo(todoItem)
    }
}


class InputViewModel: ViewModel() {

    private val _todo: MutableLiveData<String> = MutableLiveData("")
    private val _text: MutableLiveData<String> = MutableLiveData("")
    val todo: LiveData<String> = _todo
    val text: LiveData<String> = _text

    fun onInputChange(newName: String) {
        _todo.value = newName
    }

    fun onInputChang(newName: String) {
        _text.value = newName
    }
}



@Composable
fun AppTopBar(onClick: () -> Unit, onClickAdd: () -> Unit) {
    TopAppBar(
        title = { Text(" Memo Add", color = Color.White) },
        elevation = 12.dp,
        backgroundColor = Color.Red,
        actions = {
            IconButton(onClick = onClickAdd) {
                Icon( painter = painterResource(id = R.drawable.ic_baseline_save_24), contentDescription = "save")
                //Text(text = "SAVE")
            }
        },
        navigationIcon = {

            IconButton(onClick = onClick) {
                Icon( painter = painterResource(id = R.drawable.ic_baseline_west_24), contentDescription = "back state")
                //Text(text = "SAVE")
            }
        }

    )
}

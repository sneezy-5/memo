package com.example.memo.components.updateMemo


import android.app.Application
import android.view.KeyEvent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.memo.R
import com.example.memo.components.AddMemo.insertTodoInDB
import com.example.memo.components.navigation.Destinations
import com.example.memo.database.TodoItem
import com.example.memo.database.TodoViewModel
import com.example.memo.database.TodoViewModelFactory



@Composable
fun AddView(navController: NavController, model: InputViewModel = viewModel(), todo:TodoItem, navigateUp: () -> Unit) { // 2.
    //val inputViewModel = InputViewModel(todo)
    val count:String by model.todo.observeAsState(todo.itemName) // 3.
    val text: String by model.text.observeAsState(todo.itemText)
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val mTodoViewModel: TodoViewModel = viewModel(
        factory = TodoViewModelFactory(context.applicationContext as Application)
    )
    fun onclik(){
        if(model.todo.value.isNullOrEmpty() and model.text.value.isNullOrEmpty()){
            insertTodoInDB(
            todo.itemName,
            todo.itemText,
            mTodoViewModel
        ) }else if (model.todo.value.isNullOrEmpty()){
            insertTodoInDB(
            todo.itemName,
            model.text.value.toString(),
            mTodoViewModel
        )
        }else if (model.text.value.isNullOrEmpty()){
            insertTodoInDB(
                model.todo.value.toString(),
                todo.itemText,
                mTodoViewModel
            )
        }else{
            insertTodoInDB(
                model.todo.value.toString(),
                model.text.value.toString(),
                mTodoViewModel
            )
        }


        mTodoViewModel.deleteTodo(todo)
    }
    Scaffold(
        topBar = {
            com.example.memo.components.AddMemo.AppTopBar(onClick = {
                navigateUp()
                navController.navigate(Destinations.Home)
            }, onClickAdd = {
                onclik()
                navigateUp()
                navController.navigate(Destinations.Home)
            })
        }
   /*     floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateUp(); onclik()
                    //mTodoViewModel.updateTodo(model.to)
                    navController.navigate(Destinations.Home)
                    //navController.enableOnBackPressed(false)



                },
                backgroundColor = Color.Red,
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_save_24),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            )
        }*/) {

        InputFieldState(todo = todo, inputViewModel = model)
    }

}


@Composable
fun InputFieldState(inputViewModel: InputViewModel = viewModel(), todo:TodoItem) {
    val count:String by inputViewModel.todo.observeAsState(todo.itemName) // 3.
    val text: String by inputViewModel.text.observeAsState(todo.itemText)
    //val todo: String by inputViewModel.todo.observeAsState("")
    val focusRequester = FocusRequester()
    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        InputField(count, focusRequester = focusRequester) { inputViewModel.onInputChange(it) }
        InputField2(name = text, focusRequester = focusRequester ){inputViewModel.onInputChang(it)}
        Spacer(modifier = Modifier.padding(10.dp))
    }
}


@Composable
fun InputField(
    name: String,
    focusRequester: FocusRequester,
    onValChange: ((String) -> Unit)?,

) {


    val focusManager = LocalFocusManager.current

    if (onValChange != null) {
        TextField(
            value = name,
            onValueChange = onValChange
                //text = it
            ,
            textStyle = TextStyle(color = Color.Black, fontSize = 25.sp)
            ,
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
            placeholder = { Text(text = "Enter title") },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            singleLine = true,
            shape = RectangleShape, // The TextFiled has rounded corners top left and right by default
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                cursorColor = Color.Black,
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



@Composable
fun InputField2(
    name: String,
    focusRequester:FocusRequester,
    onValChange: ((String) -> Unit)?,

) {

    DisposableEffect(Unit) {
        focusRequester.requestFocus()
        onDispose { }
    }

    if (onValChange != null) {
        TextField(
            value = name,
            onValueChange = onValChange,
            modifier = Modifier
                //.weight(1f)
                //.height(50.dp)
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

/*fun insertTodoInDB(todo: String, todotext:String, mTodoViewModel: TodoViewModel) {
    if (todo.isNotEmpty()) {
        val todoItem = TodoItem(
            itemName = todo,
            isDone = false,
            itemText = todotext
        )

        mTodoViewModel.addTodo(todoItem)
    }
}*/


class InputViewModel: ViewModel() {

    private val _todo: MutableLiveData<String> = MutableLiveData()
    private val _text: MutableLiveData<String> = MutableLiveData()
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



package com.usil.myappcomponents

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.usil.myappcomponents.data.model.Todo
import com.usil.myappcomponents.ui.theme.MyAppComponentsTheme
import com.usil.myappcomponents.viewModel.TodoViewModel

// POO -> Programacion Orientada Objeto
//data class TodoItem(
//    val id: Int,
//    val name: String,
//    val isCompleted: Boolean
//)

//fun getSampleTodos(): List<TodoItem> {
//    return listOf(
//        TodoItem(1, "Comprar Adaptador", false),
//        TodoItem(2, "Lavar los servicios", true),
//        TodoItem(3, "Enviar un correo", true),
//        TodoItem(4, "Hacer mantenimiento al carror", false),
//        TodoItem(5, "Ordenar oficina", false)
//    )
//}

class TasksViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as TodoApplication
        val todoViewModel = app.createTodoViewModel()

        setContent {
            MyAppComponentsTheme {
                TodoAppView(viewModel = todoViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TodoAppView(viewModel: TodoViewModel = viewModel()) {

    val todos by viewModel.todos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current

    // Unit -> Para se ejecute solo la primera vez
    LaunchedEffect(Unit) {
        viewModel.getTodos()
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (todos.isNotEmpty()) {
                    viewModel.refreshTodos()
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Tareas") },
                colors =
                TopAppBarDefaults.topAppBarColors(containerColor = Color.Gray)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val intent = Intent(context, TodoViewActivity::class.java)
                    // pasar a un valor a otra pantalla
                    context.startActivity(intent)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar tarea"
                )
            }
        }
    ) { paddingValues ->
        TaskList(paddingValues, todos, isLoading, error)
    }
}

@Composable
fun TaskList(
    paddingValues: PaddingValues = PaddingValues(12.dp),
    todos: List<Todo>,
    isLoading: Boolean,
    error: String?
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(top = 10.dp)
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when {
                isLoading -> {
                    item {
                        Column {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Cargando tareas...")
                        }
                    }
                }

                error != null -> {
                    item {
                        Column {
                            Text(
                                text = "Error al obtener las tareas",
                                color = Color.Red,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                else -> {
                    items(todos) { todo ->
                        TodoCard(todo)
                    }
                }
            }


        }
    }
}

@Composable
fun TodoCard(todo: Todo) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth().clickable {
            val intent = Intent(context, TodoViewActivity::class.java)
            intent.putExtra("TASK_ID", todo.id)
            context.startActivity(intent)
        },
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(
                    imageVector = if (todo.isCompleted) Icons.Default.Check else Icons.Default.DateRange,
                    contentDescription = "Icono de Favorito",
                    tint = if (todo.isCompleted) Color(0xFF4CAF50) else Color.Red
                )
                Text(todo.name, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }
            if (todo.isCompleted) {
                Spacer(modifier = Modifier.padding(6.dp))
                Text("Completada", fontSize = 12.sp, color = Color(0xFF4CAF50))
            }
        }
    }
}
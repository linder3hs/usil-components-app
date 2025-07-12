package com.usil.myappcomponents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.usil.myappcomponents.data.model.Todo
import com.usil.myappcomponents.data.model.api.TodoApi
import com.usil.myappcomponents.ui.theme.MyAppComponentsTheme
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
        setContent {
            MyAppComponentsTheme {
                TodoAppView()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TodoAppView() {
    // var todos es una variable donde usamamos remember para poder guardar el
    // estado de nuestra variable mientra la app viva, como se variable va a inciar
    // vacia, requiero que sea mutable por ende usamos mutableStateOf, pero debemos
    // indicarle que tipo es, List<Todo>, recuerde que Todo es el data class que creamos
    // en la carpeta model, ahora indicamos que por defecto el valor todos se una lista vacia
    var todos by remember { mutableStateOf<List<Todo>>(emptyList()) }
    // vamos a crear un indicador para que muestre un loading mientras hacemos la peticion
    // ser un boolean
    var isLoading by remember { mutableStateOf<Boolean>(true) }
    // en caso exista un error vamos a guardar ese error en una variable para poder
    // mostrar en la UI
    var error by remember { mutableStateOf<String?>(null) }

    // para poder ejecutar codigo async en la UI se requiere un corutina
    val scope = rememberCoroutineScope()

    // configurar retrofit para usar la BASE_UL + la funcion getTodos()
    val api = remember {
        Retrofit.Builder()
            .baseUrl("https://usil-todo-api.vercel.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TodoApi::class.java)
    }

    // cuando la app inicie ejecute una funcion
    // queremos que se ejecute solo 1 vez
    // en el caso que queramos que se ejecute solo 1 vez
    // usamos Unit
    LaunchedEffect(Unit) {
        //que creemos un tarea en background para no
        // afectar la UI
        // para poder lanzar una tarea en background podemos
        // el scope donde esta instancia nuestra rutina
        scope.launch {
            try {
                val response = api.getTodos()
                // si queremos mostrar las tarear tenemos que
                // acceder al atributo data
                todos = response.data
                println("-----TODOS----")
                print(todos)
                // cuando terminamos de cargar las tareas
                // debemos pasar ese isLoading a false
                isLoading = false
            } catch (e: Exception) {
                println("ERROOOOOOOR")
                print(e.message)
                // en caso exista un error debo tambien pasar el loading pero agregando
                // el valor a error
                error = e.message
                isLoading = false
            }
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Lista de Tareas") },
            colors =
            TopAppBarDefaults.topAppBarColors(containerColor = Color.Gray)
        )
    }) { paddingValues ->
        TaskList(paddingValues, todos)
    }
}

@Composable
fun TaskList(paddingValues: PaddingValues = PaddingValues(12.dp), todos: List<Todo>) {
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
            items(todos) { todo ->
                TodoCard(todo)
            }
        }
    }
}

@Composable
fun TodoCard(todo: Todo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),) {
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
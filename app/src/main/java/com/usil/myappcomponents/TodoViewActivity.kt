package com.usil.myappcomponents

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.usil.myappcomponents.ui.theme.MyAppComponentsTheme
import com.usil.myappcomponents.viewModel.ApiTodoResult
import com.usil.myappcomponents.viewModel.TodoViewModel

class TodoViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppComponentsTheme {
                TodoMainView()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TodoMainView(viewModel: TodoViewModel = viewModel()) {
    val context = LocalContext.current

    // Estados locales
    var taskName by remember { mutableStateOf("") }
    var isCompleted by remember { mutableStateOf(false) }
    var isNameError by remember { mutableStateOf(false) }

    // Llamar a las variables del viewModel
    val isLoading by viewModel.isLoading.collectAsState()
    val upsertResult by viewModel.upsertResult.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(upsertResult) {
        upsertResult?.let { result ->
            when (result) {
                is ApiTodoResult.Success -> {
                    val message = "Tarea creada de forma exitosa"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT)
                        .show()
                    (context as Activity).finish()
                }

                is ApiTodoResult.Error -> {
                    val message = "Hubo un error ${result.message}"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Crear tarea")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        (context as Activity).finish()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver al inicio"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.primary),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        content = { paddingValues ->
            FormUpsertView(
                paddingValues,
                taskName,
                isCompleted,
                isNameError,
                onTaskNameChange = { taskName = it },
                onIsCompleteChange = { isCompleted = it },
                onIsNameError = { isNameError = it },
                onSaveTask = {
                    viewModel.createTodo(taskName, isCompleted)
                }
            )
        }
    )
}

@Composable
fun FormUpsertView(
    paddingValues: PaddingValues,
    taskName: String,
    isCompleted: Boolean,
    isNameError: Boolean,
    onTaskNameChange: (String) -> Unit,
    onIsCompleteChange: (Boolean) -> Unit,
    onIsNameError: (Boolean) -> Unit,
    onSaveTask: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(top = 20.dp)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        OutlinedTextField(
            value = taskName,
            onValueChange = { newValue ->
                onTaskNameChange(newValue)
                onIsNameError(newValue.isBlank())
            },
            label = {
                Text("Nombre de la tarea")
            },
            placeholder = {
                Text("Ej: Comprar para la semana")
            },
            modifier = Modifier.fillMaxWidth(),
            isError = isNameError,
            supportingText = {
                if (isNameError) {
                    Text(
                        text = "El nombre es requerido",
                        color = Color.Red
                    )
                }
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isCompleted,
                onCheckedChange = { newValue ->
                    onIsCompleteChange(newValue)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = colorResource(
                        R.color.purple_500
                    )
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Marcar la tarea como completada",
                fontSize = 16.sp
            )
        }
        Button(
            onClick = {
                if (taskName.isNotBlank()) {
                    onSaveTask()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.primary)
            ),
            enabled = taskName.isNotBlank()
        ) {
            Text("Guardar Tarea")
        }
    }
}
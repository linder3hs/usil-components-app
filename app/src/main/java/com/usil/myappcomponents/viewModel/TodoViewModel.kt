package com.usil.myappcomponents.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usil.myappcomponents.data.model.Todo
import com.usil.myappcomponents.data.model.TodoUpsert
import com.usil.myappcomponents.data.model.api.TodoApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// heredar de ViewModel
class TodoViewModel : ViewModel() {
    // STADOS: Permite manipular la renderización UI
    val api: TodoApi = Retrofit.Builder()
        .baseUrl("https://usil-todo-api.vercel.app/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TodoApi::class.java)

    // Estados privado (Estas solo se puede acceder desde la misma clase)
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    // upsert = create || update
    private val _upsertResult = MutableStateFlow<ApiTodoResult?>(null)
    private val _selectedTodo = MutableStateFlow<Todo?>(null)

    // Estados publicos
    val todos: StateFlow<List<Todo>> = _todos.asStateFlow()
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    val error: StateFlow<String?> = _error.asStateFlow()
    val upsertResult: StateFlow<ApiTodoResult?> = _upsertResult.asStateFlow()
    val selectedTodo: StateFlow<Todo?> = _selectedTodo.asStateFlow()

    fun getTodos() {
        // bloque para ejecutar tareas async
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val response = api.getTodos()

                if (response.success) {
                    _todos.value = response.data
                } else {
                    _error.value = "Hubo un error al obtener las tareas"
                }
            } catch (e: Exception) {
                _error.value = "Error en el sistema, comunicate con un admin"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createTodo(name: String, isCompleted: Boolean = false) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                _upsertResult.value = null

                val todoRequest = TodoUpsert(
                    name = name,
                    isCompleted = isCompleted
                )

                val response = api.createTodo(todoRequest)

                if (response.success) {
                    // actualiza la lista de todos con el nuevo elemento
                    val currentTodos = _todos.value.toMutableList()
                    currentTodos.add(response.data)
                    _todos.value = currentTodos

                    _upsertResult.value = ApiTodoResult.Success(response.data)
                }
            } catch (e: Exception) {
                val errorMessage = "Error al crear la tarea"
                _error.value = errorMessage
                _upsertResult.value = ApiTodoResult.Error(errorMessage)
            } finally {
                _isLoading.value = false
            }
        }
    }
}

sealed class ApiTodoResult {
    data class Success(val todo: Todo) : ApiTodoResult()
    data class Error(val message: String) : ApiTodoResult()
}
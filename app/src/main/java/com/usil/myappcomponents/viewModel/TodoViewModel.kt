package com.usil.myappcomponents.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.usil.myappcomponents.data.model.Todo
import com.usil.myappcomponents.data.model.TodoUpsert
import com.usil.myappcomponents.domain.repository.TodoRepository
import com.usil.myappcomponents.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// heredar de ViewModel
class TodoViewModel(
    private val todoRepository: TodoRepository
) : ViewModel() {

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
            _isLoading.value = true
            _error.value = null

            when (val result = todoRepository.getTodos()) {
                is Result.Success -> {
                    _todos.value = result.data
                }

                is Result.Error -> {
                    _error.value = result.message
                }

                is Result.Loading -> {
                    // TODO: Replace loading logic
                }
            }

            _isLoading.value = false
        }
    }

    fun createTodo(name: String, isCompleted: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _upsertResult.value = null

            val todoRequest = TodoUpsert(
                name = name,
                isCompleted = isCompleted
            )

            when (val result = todoRepository.createTodo(todoRequest)) {
                is Result.Success -> {
                    val currentTodos = _todos.value.toMutableList()
                    currentTodos.add(result.data)
                    _todos.value = currentTodos

                    _upsertResult.value = ApiTodoResult.Success(result.data)
                }

                is Result.Error -> {
                    _error.value = result.message
                    _upsertResult.value = ApiTodoResult.Error(result.message)
                }

                is Result.Loading -> {
                    // TODO: Replace loading logic
                }
            }
            _isLoading.value = false
        }
    }

    fun updateTodo(name: String, isCompleted: Boolean, taskId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _upsertResult.value = null

            val todoRequest = TodoUpsert(
                name = name,
                isCompleted = isCompleted
            )

            when (val result = todoRepository.updatedTodo(taskId, todoRequest)) {
                is Result.Success -> {
                    val currentTodos = _todos.value.toMutableList()
                    currentTodos.add(result.data)
                    _todos.value = currentTodos

                    _upsertResult.value = ApiTodoResult.Success(result.data)
                }

                is Result.Error -> {
                    _error.value = result.message
                    _upsertResult.value = ApiTodoResult.Error(result.message)
                }

                is Result.Loading -> {
                    // TODO: Replace loading logic
                }
            }
            _isLoading.value = false
        }
    }

    fun getTodoById(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            when (val resut = todoRepository.getTodoById(id)) {
                is Result.Success -> {
                    _selectedTodo.value = resut.data
                }
                is Result.Error -> {
                    _error.value = resut.message
                }
                is Result.Loading -> {
                    // TODO: Replace loading logic
                }
            }
            _isLoading.value = false
        }
    }

    fun refreshTodos() {
        getTodos()
    }
}

sealed class ApiTodoResult {
    data class Success(val todo: Todo) : ApiTodoResult()
    data class Error(val message: String) : ApiTodoResult()
}
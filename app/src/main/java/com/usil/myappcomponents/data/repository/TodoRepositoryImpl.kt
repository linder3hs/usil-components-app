package com.usil.myappcomponents.data.repository

import com.usil.myappcomponents.data.api.TodoApi
import com.usil.myappcomponents.data.model.Todo
import com.usil.myappcomponents.data.model.TodoUpsert
import com.usil.myappcomponents.domain.repository.TodoRepository
import com.usil.myappcomponents.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * En la implementacion del interface
 * TodoRepository, vamos a usar el API
 */
class TodoRepositoryImpl(
    private val apiService: TodoApi
): TodoRepository {

    /**
     * Para la implementacion de cada funcion
     * haremos un override de cada una
     */
    override suspend fun getTodos():
            Result<List<Todo>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getTodos()

            if (response.success) {
                Result.Success(response.data)
            } else {
                Result.Error(message = "Error al obtener las tareas")
            }
        } catch (e: Exception) {
            Result.Error(message = "Error en el servidor", throwable = e)
        }
    }

    override suspend fun getTodoById(id: Int):
            Result<Todo> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getTodoById(id)

            if (response.success) {
                Result.Success(response.data)
            } else {
                Result.Error("Error al obtener la tarea")
            }
        } catch (e: Exception) {
            Result.Error(message = "Error en el servidor", throwable = e)
        }
    }

    override suspend fun createTodo(todoUpsert: TodoUpsert):
            Result<Todo> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.createTodo(todoUpsert)

            if (response.success) {
                Result.Success(response.data)
            } else {
                Result.Error("Error al crear la tarea")
            }
        } catch (e: Exception) {
            Result.Error(message = "Error en el servidor", throwable = e)
        }
    }

    override suspend fun updatedTodo(id: Int, todoUpsert: TodoUpsert):
            Result<Todo> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.updateTodo(id, todoUpsert)

            if (response.success) {
                Result.Success(response.data)
            } else {
                Result.Error("Error al actualizar la tarea")
            }
        } catch (e: Exception) {
            Result.Error(message = "Error en el servidor", throwable = e)
        }
    }
}
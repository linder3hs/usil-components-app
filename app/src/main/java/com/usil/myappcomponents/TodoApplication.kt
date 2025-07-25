package com.usil.myappcomponents

import android.app.Application
import com.usil.myappcomponents.data.api.TodoApi
import com.usil.myappcomponents.data.repository.TodoRepositoryImpl
import com.usil.myappcomponents.domain.repository.TodoRepository
import com.usil.myappcomponents.viewModel.TodoViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Usando el singleton pattern
 * vamos a crear la instancia de retrofit
 * para usarla en cualquier vista
 */

class TodoApplication: Application() {

    val todoRepository: TodoRepository by lazy {
        val api = Retrofit.Builder()
            .baseUrl("https://usil-todo-api.vercel.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TodoApi::class.java)

        TodoRepositoryImpl(api)
    }

    fun createTodoViewModel(): TodoViewModel {
        return TodoViewModel(todoRepository)
    }
}
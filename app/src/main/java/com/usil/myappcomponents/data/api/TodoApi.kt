package com.usil.myappcomponents.data.api

import com.usil.myappcomponents.data.model.ApiResponse
import com.usil.myappcomponents.data.model.ApiResponses
import com.usil.myappcomponents.data.model.TodoUpsert
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TodoApi {
    /*
    llamar al endpint todos para listar tareas
    vamos a crear la funcion para poder listar las tareas
    usando suspend
    la funcion getTodos va a retornar nuestro data class ApiResponse
    de la interface se define las funciones que vamos a usar, pero
    no se hace la logica que se ejecutara
    */
    @GET("todos")
    suspend fun getTodos(): ApiResponses

    @GET("todos/{id}")
    suspend fun getTodoById(@Path("id") id: Int): ApiResponse

    @POST("todos")
    suspend fun createTodo(@Body todoUpsert: TodoUpsert): ApiResponse

    @PUT("todos/{id}")
    suspend fun updateTodo(@Path("id") id: Int, @Body todoUpsert: TodoUpsert): ApiResponse

    @DELETE("todos/{id}")
    suspend fun deleteTodo(@Path("id") id: Int): ApiResponse


}
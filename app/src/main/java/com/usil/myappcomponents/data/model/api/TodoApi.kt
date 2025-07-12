package com.usil.myappcomponents.data.model.api

import com.usil.myappcomponents.data.model.ApiResponse
import retrofit2.http.GET

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
    suspend fun getTodos(): ApiResponse

    // aca van a estar tambien las funciones para editar, crear o eliminar
    // crear -> post
    // editar -> put / patch
    // eliminar -> eliminar
}
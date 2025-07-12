package com.usil.myappcomponents.data.model

// Vamos a crear el data class para
// ApiResponse y Todo
data class ApiResponse(
    val success: Boolean,
    val data: List<Todo>,
    val total: Int
)

data class Todo(
    val id: Int,
    val name: String,
    val isCompleted: Boolean
)
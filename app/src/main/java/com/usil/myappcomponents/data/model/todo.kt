package com.usil.myappcomponents.data.model

import com.google.gson.annotations.SerializedName

// Vamos a crear el data class para
// ApiResponse y Todo
data class ApiResponses(
    val success: Boolean,
    val data: List<Todo>,
    val total: Int
)

data class ApiResponse(
    val success: Boolean,
    val data: Todo,
    val message: String
)

data class Todo(
    val id: Int,
    val name: String,
    @SerializedName("is_completed")
    val isCompleted: Boolean,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class TodoUpsert (
    val name: String,
    @SerializedName("is_completed")
    val isCompleted: Boolean
)
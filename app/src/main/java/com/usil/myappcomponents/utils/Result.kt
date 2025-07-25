package com.usil.myappcomponents.utils

/**
 * T: Generic Type
 * - Esta clase buscar reducir el uso de Try Catch
 * - Tener un manejo acdecuado de errores
 * - Codigo mas limpio y mantenible
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(
        val message: String,
        val throwable: Throwable? = null)
        : Result<Nothing>()

    object Loading: Result<Nothing>()
}

inline fun <T> Result<T>.onSuccess(
    action: (value: T) -> Unit): Result<T> {
    if (this is Result.Success) action (data)
    return  this
}

inline fun <T> Result<T>.onError(
    action: (message: String) -> Unit): Result<T> {
    if (this is Result.Error) action(message)
    return this
}

// Manejo de nulls
fun <T> Result<T>.getDataOrNull(): T? {
    return if (this is Result.Success) data else null
}

// Verificaciones de estado
// siempre que usamos operadores de comparación estos retorna un bool
fun <T> Result<T>.isSuccess(): Boolean = this is Result.Success
fun <T> Result<T>.isError(): Boolean = this is Result.Error
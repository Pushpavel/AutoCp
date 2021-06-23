package common.errors

/**
 * General Result Type
 *
 * inspired from https://kotlin.christmas/2019/17
 * though com.jetbrains.rd.util.Result has similar implementation, it cannot be extended
 */

class Ok<out T>(val value: T) : Result<T>, TpResult<T>

sealed class Err(message: String) : Throwable(message), Result<Nothing>

sealed interface Result<out T> {
    fun getOrNull() = if (this is Ok) value else null
}
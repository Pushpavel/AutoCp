package common.errors

/**
 * inspired from https://kotlin.christmas/2019/17
 * though com.jetbrains.rd.util.Result has similar implementation, it cannot be extended
 *
 * General Result Type
 */
sealed interface Result<out T>

class Ok<out T> : Result<T>, TpResult<T>

sealed class Err(message: String) : Throwable(message), Result<Nothing>

package common.errors

/**
 * inspired from https://kotlin.christmas/2019/17
 * though com.jetbrains.rd.util.Result has similar implementation, it cannot be extended
 */
sealed class Result<T> {
    class Ok<T>(val value: T) : Result<T>()
    class Err(val error: Throwable) : Result<Nothing>()
}
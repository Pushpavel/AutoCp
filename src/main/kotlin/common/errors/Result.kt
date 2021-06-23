package common.errors

/**
 * General Result Type
 *
 * inspired from https://kotlin.christmas/2019/17
 * though com.jetbrains.rd.util.Result has similar implementation, it cannot be extended
 */

class Ok<out T>(val value: T) : Result<T>, TpResult<T>

sealed class Err : Exception, Result<Nothing> {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class Uncaught(throwable: Throwable) : Err(throwable)
}

sealed interface Result<out T>
package common.errors


inline fun <T, R> T.catch(block: T.() -> R): Result<R> {
    return try {
        Ok(block())
    } catch (e: Throwable) {
        Err.Uncaught(e)
    }
}

fun <T> Result<T>.getOrNull() = if (this is Ok) value else null
fun <T> Result<T>.errOrNull() = if (this is Err) this else null
fun <T> Result<T>.getOrThrow() = if (this is Err) throw this else this

inline fun <T> Result<T>.onErr(action: (e: Err) -> Unit): Result<T> {
    errOrNull()?.let { action(it) }
    return this
}
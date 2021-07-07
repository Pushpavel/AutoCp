package common.errors

fun <T> Result<T>.errOrNull(): Err? {
    return this.exceptionOrNull()?.mapToErr()
}
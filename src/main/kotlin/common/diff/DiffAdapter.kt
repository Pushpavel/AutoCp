package common.diff

fun interface DiffAdapter<T> {
    fun isSame(item1: T, item2: T): Boolean

    fun isEqual(item1: T, item2: T): Boolean = item1 == item2
}
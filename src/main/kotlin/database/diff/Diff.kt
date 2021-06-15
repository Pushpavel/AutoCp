package database.diff

interface Diff<T> {

    fun isSame(item1: T, item2: T): Boolean

    fun isEqual(item1: T, item2: T): Boolean

    fun compute(from: Iterable<T>, to: List<T>, listener: Listener<T>)

    interface Listener<T> {

        fun add(item: T)

        fun remove(item: T)

        fun update(oldItem: T, item: T)

    }
}
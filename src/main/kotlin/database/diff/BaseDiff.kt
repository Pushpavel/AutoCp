package database.diff

import java.util.*

abstract class BaseDiff<T> : Diff<T> {

    override fun compute(from: Iterable<T>, to: List<T>, listener: Diff.Listener<T>) {
        val updateList = LinkedList(to)
        for (item1 in from) {

            val item2 = updateList.find { isSame(item1, it) }

            if (item2 == null) {
                listener.remove(item1)
                continue
            }

            updateList.remove(item2)
            if (!isEqual(item1, item2))
                listener.update(item1, item2)
        }

        for (item in updateList)
            listener.add(item)
    }

}
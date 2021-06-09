package ui

abstract class BasePopListListener<T> : PopListAdapter.Listener<T> {
    override fun add(index: Int, item: T) {}

    override fun remove(index: Int, item: T) {}

    override fun update(index: Int, item: T) {}

    override fun select(selection: Int, item: T?) {}

}
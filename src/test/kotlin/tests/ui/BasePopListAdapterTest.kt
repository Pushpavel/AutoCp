@file:Suppress("ClassName")

package tests.ui

import com.intellij.openapi.editor.event.SelectionListener
import io.mockk.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import ui.BasePopListAdapter
import ui.BasePopListListener
import javax.swing.event.ListSelectionListener

internal class BasePopListAdapterTest {

    lateinit var adapter: BasePopListAdapter<ItemStub>
    private lateinit var item: ItemStub
    private lateinit var item2: ItemStub
    private lateinit var item3: ItemStub
    private lateinit var list: List<ItemStub>
    private lateinit var callback: BasePopListListener<ItemStub>
    private lateinit var nameCallback: NameListListenerImpl

    @BeforeEach
    fun setUp() {
        adapter = BasePopListAdapterImpl()
        item = ItemStub("Name1", 1)
        item2 = ItemStub("Name2", 2)
        item3 = ItemStub("Name3", 3)
        list = listOf(item, item2, item3)

        callback = mockk(relaxed = true)
        nameCallback = spyk(NameListListenerImpl())

        adapter.addListEventsListener(callback)
        adapter.getNameListModel().addListDataListener(nameCallback)

        resetMockEnv()
    }


    @Nested
    inner class addItem {
        @Test
        fun itemsAddedToList() {
            adapter.addItem(0, item)
            adapter.addItem(1, item2)
            adapter.addItem(1, item3)

            checkItems(item, item3, item2)
        }

        @Test
        fun addEventsFired() {
            adapter.addItem(0, item)
            adapter.addItem(1, item2)
            adapter.addItem(1, item3)

            verifySequence {
                callback.add(0, item)
                callback.add(1, item2)
                callback.add(1, item3)
            }
        }

        @Test
        fun noExceptionOnIllegalIndex() {
            assertDoesNotThrow { adapter.addItem(4, item) }
            confirmVerified(callback)
            val result = adapter.addItem(4, item)
            assertEquals(false, result)
        }

        @Test
        fun nameListModelUpdated() {
            adapter.addItem(0, item)
            adapter.addItem(1, item2)
            adapter.addItem(1, item3)

            checkNames("Name1", "Name3", "Name2")

            verifySequence {
                nameCallback.add(0)
                nameCallback.add(1)
                nameCallback.add(1)
            }
        }
    }


    @Nested
    inner class removeItem {

        @Test
        fun itemsRemovedFromList() {
            addListToAdapter()

            assertEquals(item2, adapter.removeItem(1))

            checkItems(item, item3)
        }

        @Test
        fun removeEventsFired() {
            addListToAdapter()

            adapter.removeItem(2)
            adapter.removeItem(1)
            adapter.removeItem(0)

            verifySequence {
                callback.remove(2, item3)
                callback.remove(1, item2)
                callback.remove(0, item)
            }
        }

        @Test
        fun noExceptionOnIllegalIndex() {
            assertDoesNotThrow { adapter.removeItem(4) }
            confirmVerified(callback)
            val result = adapter.removeItem(0)
            assertEquals(null, result)
        }

        @Test
        fun nameListModelUpdated() {
            addListToAdapter()

            adapter.removeItem(1)
            adapter.removeItem(0)

            checkNames("Name3")

            verifySequence {
                nameCallback.remove(1)
                nameCallback.remove(0)
            }
        }
    }


    @Nested
    inner class updateItem {
        @Test
        fun itemUpdatedAndEventFired() {
            addListToAdapter()

            val newItem = ItemStub("New Item", 80)
            val result = adapter.updateItem(1, newItem)
            assertEquals(true, result)
            checkItems(item, newItem, item3)
            verifySequence {
                callback.update(1, newItem)
            }
        }

        @Test
        fun doesNothingOnUpdatingUnchangedItem() {
            addListToAdapter()

            val equalItem = item2.copy()
            val result = adapter.updateItem(1, equalItem)
            assertEquals(false, result)
            checkItems(*list.toTypedArray())
            confirmVerified(callback)
        }

        @Test
        fun nameListModelUpdated() {
            addListToAdapter()

            val changedItem = ItemStub("ChangedItem", 333)
            adapter.updateItem(1, changedItem)

            checkNames("Name1", "ChangedItem", "Name3")

            verifySequence {
                nameCallback.update(1)
            }
        }
    }


    @Nested
    inner class selectItem {
        @Test
        fun itemSelected() {
            addListToAdapter()
            assertEquals(-1, adapter.getSelectedIndex())
            adapter.selectItem(2)
            assertEquals(2, adapter.getSelectedIndex())
        }

        @Test
        fun selectEventFired() {
            addListToAdapter()
            adapter.selectItem(0)
            adapter.selectItem(1)
            verifySequence {
                callback.select(0, item)
                callback.select(1, item2)
            }
        }

        @Test
        fun noExceptionOnIllegalIndex() {
            assertDoesNotThrow { adapter.selectItem(0) }
            confirmVerified(callback)
            val result = adapter.selectItem(4)
            assertEquals(false, result)
        }

        @Test
        fun selectionModelUpdate() {
            addListToAdapter()
            adapter.selectItem(1)

            assertEquals(1, adapter.getSelectedIndex())
        }
    }


    @Nested
    inner class submitList {

        @Test
        fun itemsAdded() {
            adapter.submitList(list)
            checkItems(*list.toTypedArray())
            verifySequence {
                callback.add(0, item)
                callback.add(1, item2)
                callback.add(2, item3)
            }
        }

        @Test
        fun onlyChangedItemsUpdated() {
            adapter.submitList(list)
            resetMockEnv()

            // making copy of items
            val newList = ArrayList(list).map { it.copy() }.toMutableList()
            adapter.submitList(newList)
            // nothing should have happened
            checkItems(*list.toTypedArray())
            confirmVerified(callback)

            val changedItem = ItemStub("ChangedItem", 45)
            newList[1] = changedItem

            adapter.submitList(newList)
            checkItems(item, changedItem, item3)
            verifySequence { callback.update(1, changedItem) }
        }

        @Test
        fun newItemsAdded() {
            adapter.submitList(list)
            resetMockEnv()

            val newList = list.toMutableList()
            val newItem = ItemStub("New Item", 332)
            newList.add(newItem)

            adapter.submitList(newList)

            checkItems(*list.toTypedArray(), newItem)
            verifySequence { callback.add(3, newItem) }
        }

        @Test
        fun removedItemsRemoved() {
            adapter.submitList(list)
            resetMockEnv()

            val newList = list.toMutableList()
            newList.removeLast()

            adapter.submitList(newList)
            checkItems(item, item2)
            verifySequence { callback.remove(2, item3) }
        }
    }


    // Utils
    private fun checkItems(vararg items: ItemStub) = assertArrayEquals(items, adapter.getItems().toArray())

    private fun checkNames(vararg names: String) =
        assertArrayEquals(names, adapter.getNameListModel().items.toTypedArray())


    private fun addListToAdapter() {
        adapter.addItem(0, item)
        adapter.addItem(1, item2)
        adapter.addItem(2, item3)
        resetMockEnv()
    }

    private fun resetMockEnv() {
        clearMocks(callback)
        clearMocks(nameCallback)
        excludeRecords {
            nameCallback.contentsChanged(any())
            nameCallback.intervalAdded(any())
            nameCallback.intervalRemoved(any())
        }
    }
}
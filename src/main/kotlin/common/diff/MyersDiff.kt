@file:Suppress("LocalVariableName")

package common.diff

import java.util.*

class MyersDiff<T>(private val adapter: DiffAdapter<T>) {

    fun compute(oldList: List<T>, newList: List<T>): List<Delta> {
        val endNode = buildPath(oldList, newList)
        return buildDeltas(endNode, oldList, newList)
    }

    private fun buildPath(oldList: List<T>, newList: List<T>): PathNode {
        val N = oldList.size
        val M = newList.size
        val maxD = N + M
        val endNodes = HashMap<Int, PathNode>(maxD)
        endNodes[1] = PathNode(0, -1, null)

        for (d in 0..maxD) {
            for (k in -d..d step 2) {
                val down = k == -d || k != d && endNodes.getValue(k - 1).x < endNodes.getValue(k + 1).x
                val prevK = if (down) k + 1 else k - 1

                val prevNode = endNodes.getValue(prevK)
                var x = if (down) prevNode.x else prevNode.x + 1
                var y = x - k


                var node = PathNode(x, y, if (prevNode.y != -1) prevNode else null)

                while (x < N && y < M && adapter.isSame(oldList[x], newList[y])) {
                    x++
                    y++
                }

                if (x > node.x)
                    node = PathNode(x, y, node)

                endNodes[k] = node

                if (x >= N && y >= M)
                    return endNodes.getValue(k)
            }
        }

        throw IllegalStateException("Could not find Diff")
    }

    private fun buildDeltas(endNode: PathNode, oldList: List<T>, newList: List<T>): List<Delta> {
        val deltas = buildMutableDeltas(endNode, oldList, newList)
        return deltas.map { Delta(it.x, it.y, it.length, it.type) }
    }


    private fun buildMutableDeltas(endNode: PathNode, oldList: List<T>, newList: List<T>): LinkedList<MutableDelta> {
        val deltas = LinkedList<MutableDelta>()
        var node = endNode
        var prevNode = node.prev

        while (prevNode != null) {
            val delta = deltas.firstOrNull()
            val insert = node.x == prevNode.x
            val delete = node.y == prevNode.y

            if (insert) {
                if (delta?.type == DeltaType.INSERT) {
                    delta.x = prevNode.x
                    delta.y = prevNode.y
                    delta.length++
                } else
                    deltas.push(MutableDelta(prevNode.x, prevNode.y, 1, DeltaType.INSERT))
            } else if (delete) {
                if (delta?.type == DeltaType.DELETE) {
                    delta.x = prevNode.x
                    delta.y = prevNode.y
                    delta.length++
                } else
                    deltas.push(MutableDelta(prevNode.x, prevNode.y, 1, DeltaType.DELETE))
            } else if (delta?.type == DeltaType.NULL)
                throw IllegalStateException()
            else {
                var x = node.x - 1
                var y = node.y - 1
                var equalLength = 0
                var updateLength = 0

                while (x >= prevNode.x) {
                    if (adapter.isEqual(oldList[x], newList[y])) {
                        if (updateLength > 0) {
                            deltas.push(MutableDelta(x, y, updateLength, DeltaType.UPDATE))
                            updateLength = 0
                        }
                        equalLength++
                    } else {
                        if (equalLength > 0) {
                            deltas.push(MutableDelta(x, y, equalLength, DeltaType.NULL))
                            equalLength = 0
                        }
                        updateLength++
                    }

                    x--
                    y--
                }

                if (updateLength > 0) deltas.push(MutableDelta(x + 1, y + 1, updateLength, DeltaType.UPDATE))
                if (equalLength > 0) deltas.push(MutableDelta(x + 1, y + 1, equalLength, DeltaType.NULL))
            }
            node = prevNode
            prevNode = node.prev
        }

        return deltas
    }

}

data class PathNode(
    val x: Int,
    val y: Int,
    val prev: PathNode?
)

data class Delta(
    val x: Int,
    val y: Int,
    val length: Int,
    val type: DeltaType,
)

data class MutableDelta(
    var x: Int,
    var y: Int,
    var length: Int,
    val type: DeltaType,
)

enum class DeltaType {
    INSERT, DELETE, UPDATE, NULL
}
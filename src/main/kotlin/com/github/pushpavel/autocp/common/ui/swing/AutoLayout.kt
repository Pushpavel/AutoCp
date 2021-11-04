package com.github.pushpavel.autocp.common.ui.swing

import java.awt.*

/**
 * Swing layout manager similar to FlowLayout but with following differences
 * - top-left alignment
 * - respects minimum sizes of children
 * - flows in vertical or horizontal direction
 * - has more configuration options
 */
class AutoLayout(
    private val mainAxis: MainAxis = MainAxis.HORIZONTAL,
    var mainGap: Int = 8,
    var crossGap: Int = 8,
    var autoFitMainLines: Boolean = false,
    var uniformCrossLength: Boolean = true,
) : LayoutManager {

    enum class MainAxis { HORIZONTAL, VERTICAL }
    data class Placement(var offset: Int, var size: Int)

    override fun addLayoutComponent(name: String?, comp: Component?) {}
    override fun removeLayoutComponent(comp: Component?) {}

    override fun preferredLayoutSize(parent: Container) = calculateSize(parent) { it.preferredSize ?: Dimension(0, 0) }
    override fun minimumLayoutSize(parent: Container) = calculateSize(parent) { it.minimumSize ?: Dimension(0, 0) }

    override fun layoutContainer(parent: Container) {
        val maxMain = parent.size.main - parent.insets.mainStart - parent.insets.mainEnd

        val children = parent.components.filter { it.isVisible }

        var currLineStartIndex = 0
        var currCross = 0

        while (currLineStartIndex < children.size) {
            val placements = calculateNextMainLine(maxMain, children, currLineStartIndex)

            if (placements.isEmpty()) {
                // could not fit the component even in one full line with its preferred length
                // so fill one full line with this component respecting its minimum length
                placements.add(Placement(0, maxOf(maxMain, children[currLineStartIndex].minimumSize?.main ?: 0)))
            }

            val maxCrossLengthOfLine =
                calculateMaxPreferredCrossLengthOfLine(children, currLineStartIndex, placements.size)

            if (autoFitMainLines)
                autoFitPlacementsInMain(maxMain, placements)

            val uniformCrossLength = if (uniformCrossLength) maxCrossLengthOfLine else 0

            placeNextMainLine(currCross, children, currLineStartIndex, placements, uniformCrossLength)
            currLineStartIndex += placements.size
            currCross += maxCrossLengthOfLine + crossGap
        }
    }

    private fun calculateMaxPreferredCrossLengthOfLine(children: List<Component>, startIndex: Int, size: Int): Int {
        var maxCross = 0
        for (i in startIndex until startIndex + size) {
            val child = children[i]
            maxCross = maxOf(maxCross, child.preferredSize.cross)
        }
        return maxCross
    }

    /**
     * Calculates list of component offset and length in the main axis for a new line
     */
    private fun calculateNextMainLine(
        maxMain: Int,
        children: List<Component>,
        startIndex: Int,
        getChildSize: (Component) -> Dimension = { it.preferredSize ?: Dimension(0, 0) }
    ): MutableList<Placement> {
        val placements = mutableListOf<Placement>()
        var main = 0
        for (i in startIndex until children.size) {
            val component = children[i]

            val gap = if (placements.isNotEmpty()) mainGap else 0
            val mainLength = getChildSize(component).main

            if (main + gap + mainLength <= maxMain) {
                placements.add(Placement(main + gap, mainLength))
                main += mainLength + gap
            } else
                break
        }
        return placements
    }

    /**
     * places the components according to values in [placements] in the main axis
     */
    private fun placeNextMainLine(
        currCross: Int,
        children: List<Component>,
        startIndex: Int,
        placements: List<Placement>,
        minCrossLength: Int = 0
    ) {
        for (i in placements.indices) {
            val placement = placements[i]
            val component = children[startIndex + i]
            component.applyBounds(
                placement.offset,
                currCross,
                placement.size,
                maxOf(minCrossLength, component.preferredSize.cross)
            )
        }
    }

    private fun autoFitPlacementsInMain(maxMain: Int, placements: List<Placement>) {
        val lastPlacement = placements[placements.lastIndex]
        val end = lastPlacement.offset + lastPlacement.size
        val remainder = maxMain - end
        if (remainder <= 0)
            return
        val extraToAdd = remainder / placements.size
        var extraSum = 0
        for (placement in placements) {
            placement.offset += extraSum
            placement.size += extraToAdd
            extraSum += extraToAdd
        }
    }


    private fun Component.applyBounds(mainStart: Int, crossStart: Int, mainLength: Int, crossLength: Int) {
        if (mainAxis == MainAxis.HORIZONTAL)
            setBounds(mainStart, crossStart, mainLength, crossLength)
        else
            setBounds(crossStart, mainStart, crossLength, mainLength)
    }

    private fun calculateSize(parent: Container, getChildSize: (Component) -> Dimension): Dimension {
        val maxMain = parent.size.main - parent.insets.mainStart - parent.insets.mainEnd
        val children = parent.components.filter { it.isVisible }

        var currLineStartIndex = 0
        var currCross = 0
        var maxMainLineEnd = 0

        while (currLineStartIndex < children.size) {
            val placements = calculateNextMainLine(maxMain, children, currLineStartIndex, getChildSize)

            if (placements.isEmpty())
                placements.add(
                    Placement(
                        0,
                        maxOf(maxMain, maxOf(maxMain, getChildSize(children[currLineStartIndex]).main))
                    )
                )

            val maxCrossLengthOfLine =
                calculateMaxPreferredCrossLengthOfLine(children, currLineStartIndex, placements.size)

            currLineStartIndex += placements.size
            currCross += maxCrossLengthOfLine + crossGap
            val lastPlacement = placements.last()
            maxMainLineEnd = maxOf(maxMainLineEnd, lastPlacement.offset + lastPlacement.size)
        }

        if (currCross > 0)
            currCross -= crossGap

        if (autoFitMainLines)
            maxMainLineEnd = maxMain

        val dim = Dimension(maxMainLineEnd, currCross)

        // add insets of parent
        dim.main += parent.insets.mainStart + parent.insets.mainEnd
        dim.cross += parent.insets.crossStart + parent.insets.crossEnd
        return dim
    }

    // Normalize Dimensions to main and cross axis
    private var Dimension.main
        get() = if (mainAxis == MainAxis.HORIZONTAL) width else height
        set(value) = if (mainAxis == MainAxis.HORIZONTAL) width = value else height = value

    private var Dimension.cross
        get() = if (mainAxis == MainAxis.HORIZONTAL) height else width
        set(value) = if (mainAxis == MainAxis.HORIZONTAL) height = value else width = value

    private val Insets.mainStart get() = if (mainAxis == MainAxis.HORIZONTAL) left else top
    private val Insets.mainEnd get() = if (mainAxis == MainAxis.HORIZONTAL) right else bottom
    private val Insets.crossStart get() = if (mainAxis == MainAxis.HORIZONTAL) top else left
    private val Insets.crossEnd get() = if (mainAxis == MainAxis.HORIZONTAL) bottom else right
}
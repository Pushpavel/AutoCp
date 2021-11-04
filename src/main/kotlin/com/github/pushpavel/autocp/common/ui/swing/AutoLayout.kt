package com.github.pushpavel.autocp.common.ui.swing

import com.intellij.util.ui.AbstractLayoutManager
import java.awt.Component
import java.awt.Container
import java.awt.Dimension
import java.awt.Insets

/**
 * Swing layout manager similar to FlowLayout but with following differences
 * - top-left alignment
 * - respects minimum sizes of children
 * - flows in vertical or horizontal direction
 */
class AutoLayout(
    private val mainAxis: MainAxis = MainAxis.HORIZONTAL,
    var mainGap: Int = 8,
    var crossGap: Int = 8
) : AbstractLayoutManager() {

    enum class MainAxis { HORIZONTAL, VERTICAL }
    enum class Flag { PUSH_MAIN }

    private val constraints = mutableMapOf<Component, Flag?>()

    override fun addLayoutComponent(comp: Component, constraint: Any?) {
        constraints[comp] = constraint as? Flag
    }

    override fun removeLayoutComponent(comp: Component) {
        constraints.remove(comp)
    }

    override fun preferredLayoutSize(parent: Container) = calculateSize(parent) { it.preferredSize ?: Dimension(0, 0) }

    override fun minimumLayoutSize(parent: Container) = calculateSize(parent) { it.minimumSize ?: Dimension(0, 0) }

    override fun maximumLayoutSize(target: Container) = calculateSize(target) { it.maximumSize ?: Dimension(0, 0) }


    override fun getLayoutAlignmentX(target: Container) = 0.5f

    override fun getLayoutAlignmentY(target: Container) = 0.5f

    override fun invalidateLayout(target: Container) {}

    override fun layoutContainer(parent: Container) {
        var main = 0
        var cross = 0
        var currCrossMax = 0
        val maxMain = parent.size.main - parent.insets.mainStart - parent.insets.mainEnd

        for (component in parent.components) {
            if (!component.isVisible) continue
            var mainWithGap = main + if (main != 0) mainGap else 0
            val constraint = constraints[component]
            val preferredMain = if (constraint == Flag.PUSH_MAIN) maxMain else component.preferredSize.main

            val (nextLine, end) = calculateMainAxisComponentEnd(
                mainWithGap,
                maxMain,
                preferredMain,
                component.minimumSize.main
            )

            if (nextLine) {
                cross += currCrossMax + crossGap
                mainWithGap = 0
                currCrossMax = 0
            }

            currCrossMax = maxOf(currCrossMax, component.preferredSize.cross)

            val cMain = parent.insets.mainStart + mainWithGap
            val cMainLength = end - mainWithGap
            component.applyBounds(cMain, cross, cMainLength, component.preferredSize.cross)

            // account for applied component size
            main = mainWithGap + cMainLength
        }
    }

    private fun Component.applyBounds(mainStart: Int, crossStart: Int, mainLength: Int, crossLength: Int) {
        if (mainAxis == MainAxis.HORIZONTAL)
            setBounds(mainStart, crossStart, mainLength, crossLength)
        else
            setBounds(crossStart, mainStart, crossLength, mainLength)
    }

    /**
     * Calculates whether to move next line and the end of the component in main axis
     */
    private fun calculateMainAxisComponentEnd(
        main: Int,
        maxMain: Int,
        preferredMain: Int,
        minimumMain: Int
    ): Pair<Boolean, Int> {
        return if (main + preferredMain <= maxMain) {
            Pair(false, main + preferredMain)
        } else if (preferredMain <= maxMain) {
            Pair(true, preferredMain)
        } else if (minimumMain <= maxMain) {
            Pair(true, maxMain)
        } else {
            Pair(true, minimumMain)
        }
    }


    private fun calculateSize(parent: Container, getChildSize: (Component) -> Dimension): Dimension {
        var main = 0
        var cross = 0
        var currCrossMax = 0
        val maxMain = parent.size.main - parent.insets.mainStart - parent.insets.mainEnd


        for (component in parent.components) {
            if (!component.isVisible) continue
            val d = getChildSize(component)
            val mainWithGap = main + if (main != 0) mainGap else 0
            val (nextLine, end) = calculateMainAxisComponentEnd(mainWithGap, maxMain, d.main, d.main)
            if (nextLine) {
                cross += currCrossMax + crossGap
                currCrossMax = 0
            }
            currCrossMax = maxOf(currCrossMax, d.cross)

            main = end
        }
        cross += currCrossMax
        val dim = Dimension(main, cross)

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
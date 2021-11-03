package com.github.pushpavel.autocp.common.ui.swing

import java.awt.Component
import java.awt.Container
import java.awt.Dimension
import java.awt.LayoutManager

/**
 * Swing layout manager similar to FlowLayout but with following differences
 * - top-left alignment
 * - respects minimum sizes of children
 */
class AutoLayout : LayoutManager {
    override fun addLayoutComponent(name: String?, comp: Component?) {
    }

    override fun removeLayoutComponent(comp: Component?) {
    }

    override fun preferredLayoutSize(parent: Container) = calculateSize(parent) { it.preferredSize }

    override fun minimumLayoutSize(parent: Container) = calculateSize(parent) { it.minimumSize }


    override fun layoutContainer(parent: Container) {
        var x = 0
        var y = 0
        var currMaxHeight = 0
        val maxWidth = parent.width - parent.insets.left - parent.insets.right

        for (component in parent.components) {
            if (!component.isVisible) continue
            val preferredSize = component.preferredSize
            val minimumSize = component.minimumSize
            var _x = x
            var _y = y
            var _width = preferredSize.width
            val _height = preferredSize.height

            if (x + preferredSize.width <= maxWidth) {
                x += _width
                currMaxHeight = maxOf(currMaxHeight, _height)
            } else if (preferredSize.width <= maxWidth) {
                y += currMaxHeight
                x = 0
                _y = y
                _x = x
                x += _width
                currMaxHeight = _height
            } else if (minimumSize.width <= maxWidth) {
                y += currMaxHeight
                x = 0
                _y = y
                _x = x
                _width = maxWidth
                x += _width
                currMaxHeight = _height
            } else {
                y += currMaxHeight
                x = 0
                _y = y
                _x = x
                _width = minimumSize.width
                x += _width
                currMaxHeight = _height
            }
            component.setBounds(parent.insets.left + _x, parent.insets.top + _y, _width, _height)
        }
    }


    private fun calculateSize(parent: Container, getChildSize: (Component) -> Dimension): Dimension {
        var x = 0
        var y = 0
        var currMaxHeight = 0
        val maxWidth = parent.width - parent.insets.left - parent.insets.right

        for (component in parent.components) {
            if (!component.isVisible) continue
            val d = getChildSize(component)
            if (x + d.width > maxWidth) {
                x = 0
                y += currMaxHeight
                currMaxHeight = 0
            } else {
                x += d.width
                currMaxHeight = maxOf(currMaxHeight, d.height)
            }
        }
        y += currMaxHeight

        val dim = Dimension(x, y)

        // add insets of parent
        dim.width += parent.insets.left + parent.insets.right
        dim.height += parent.insets.top + parent.insets.bottom
        return dim
    }

}
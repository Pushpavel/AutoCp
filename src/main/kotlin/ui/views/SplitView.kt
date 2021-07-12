package ui.views

import com.intellij.ui.OnePixelSplitter
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

open class SplitView(vertical: Boolean, proportion: Float) : OnePixelSplitter(vertical, proportion), ScopedView {
    override val viewScope = MainScope()

    override fun dispose() {
        super.dispose()
        viewScope.cancel()
    }
}
package settings

import com.intellij.ui.layout.LCFlags
import com.intellij.ui.layout.panel
import plugin.settings.SolutionLanguage
import ui.poplist.PopList.ItemView

class LanguageItemPanel : ItemView<SolutionLanguage> {

    override val component = panel(LCFlags.fillX) {

    }

    override fun updateView(item: SolutionLanguage) {
        TODO("Not yet implemented")
    }

}
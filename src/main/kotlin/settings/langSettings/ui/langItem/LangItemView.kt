package settings.langSettings.ui.langItem

import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBPanel
import kotlinx.coroutines.CoroutineScope
import settings.langSettings.model.BuildProperties
import ui.vvm.View
import ui.vvm.swingModels.toCollectionListModel
import java.awt.BorderLayout

class LangItemView : JBPanel<LangItemView>(BorderLayout()), View<LangItemViewModel> {

    val list = JBList<BuildProperties>()

    init {

        val container = ToolbarDecorator.createDecorator(list).createPanel()

        add(container, BorderLayout.CENTER)
    }

    override fun CoroutineScope.onViewModelBind(viewModel: LangItemViewModel) {
        list.model = viewModel.buildProperties.toCollectionListModel(this, viewModel.buildPropertiesChanges)
    }

}
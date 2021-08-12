package settings.projectSettings

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import settings.generalSettings.AutoCpGeneralSettings
import settings.langSettings.AutoCpLangSettings
import settings.langSettings.model.Lang

@State(
    name = "settings.Project",
    storages = [Storage("autoCpSettings.xml")]
)
@Service
class AutoCpProjectSettings(val project: Project) : PersistentStateComponent<AutoCpProjectSettings> {

    var preferredLangId: String? = AutoCpLangSettings.instance.languages.firstOrNull()?.langId
    var overridePreferredLang = false

    var shouldStartGatheringOnStart = true
    var overrideShouldStartGatheringOnStart = false

    fun guessPreferredLang(): Lang? {
        if (!overridePreferredLang)
            return AutoCpGeneralSettings.instance.getPreferredLang()

        return AutoCpLangSettings.instance.languages.run {
            if (preferredLangId != null)
                firstOrNull { it.langId == preferredLangId } ?: firstOrNull()
            else
                firstOrNull()
        }
    }

    fun shouldStartGatheringOnStart(): Boolean {
        if (!overrideShouldStartGatheringOnStart)
            return AutoCpGeneralSettings.instance.shouldStartGatheringOnStart

        return shouldStartGatheringOnStart
    }

    override fun getState() = this
    override fun loadState(state: AutoCpProjectSettings) {}
}

fun Project.autoCpProject(): AutoCpProjectSettings = service()
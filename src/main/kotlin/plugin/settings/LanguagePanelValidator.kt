package plugin.settings

class LanguagePanelValidator(private val model: SettingsModel) : LanguagePanelUI.Validator {

    override fun validateName(name: String): String? {
        return if (model.isUniqueName(name)) null else "Already Exists"
    }

    override fun validateExtension(extension: String): String? {
        return null
    }

    override fun validateBuildCommand(buildCommand: String): String? {
        val input = buildCommand.contains(AutoCpSettings.INPUT_PATH_KEY)
        val output = buildCommand.contains(AutoCpSettings.OUTPUT_PATH_KEY)

        return if (!input)
            "${AutoCpSettings.INPUT_PATH_KEY} missing"
        else if (!output)
            "${AutoCpSettings.OUTPUT_PATH_KEY} missing"
        else
            null
    }
}
package settings.langSettings.model

data class Lang(
    val langId: String,
    val fileTemplateName: String,
    val buildProperties: List<BuildProperties>,
)
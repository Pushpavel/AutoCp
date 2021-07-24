package settings.langSettings.model

data class Lang(
    val langId: String,
    val fileTemplateName: String?,
    val defaultBuildConfigId: Long?,
    val buildConfigs: List<BuildConfig>,
)
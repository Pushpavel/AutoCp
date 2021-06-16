package plugin.settings

data class SolutionLanguage(
    var name: String = "",
    var extension: String = "",
    var buildCommand: String = "",
    var id: Long = 0L,
)
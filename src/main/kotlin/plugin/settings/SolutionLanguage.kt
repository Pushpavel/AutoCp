package plugin.settings

data class SolutionLanguage(
    var name: String = "",
    var extension: String = "", // Fixme: whether extension can include . is not clear
    var buildCommand: String = "",
) {
    override fun toString(): String {
        return name
    }
}
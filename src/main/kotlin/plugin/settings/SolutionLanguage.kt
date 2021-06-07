package plugin.settings

data class SolutionLanguage(
    var name: String,
    var extension: String,
    var buildCommand: String,


    ) {
    override fun toString(): String {
        return name
    }
}
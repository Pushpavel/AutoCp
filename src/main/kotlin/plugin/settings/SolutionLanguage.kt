package plugin.settings

data class SolutionLanguage(
    var name: String,
    var buildCommand: String,
    var extension: String,


    ) {
    override fun toString(): String {
        return name
    }
}
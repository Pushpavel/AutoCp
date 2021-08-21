package common.ui.layouts

data class Stylable(val styles: MutableList<String> = mutableListOf(), val apply: Stylable.() -> Unit) {
    fun addStyle(style: String) {
        styles.add(style)
        apply()
    }
}

fun Stylable.fontSize(sizeWithUnits: String): Stylable {
    addStyle("font-size: $sizeWithUnits")
    return this
}
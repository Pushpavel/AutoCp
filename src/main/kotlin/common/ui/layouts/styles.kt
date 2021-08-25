package common.ui.layouts

data class Stylable(val styles: MutableList<String> = mutableListOf(), val apply: Stylable.() -> Unit)
package common.ui.layouts

data class HtmlTag(val children: MutableList<String> = mutableListOf())

fun html(layChildren: HtmlTag.() -> Unit): String {
    val tag = HtmlTag()
    tag.layChildren()
    return "<html>" + tag.children.joinToString("") + "</html>"
}

fun HtmlTag.bold(text: String) {
    children.add("<b>$text</b>")
}
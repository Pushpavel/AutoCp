package common.ui.layouts

data class HtmlTag(val children: MutableList<String> = mutableListOf())


fun html(layChildren: HtmlTag.() -> Unit): String {
    val tag = HtmlTag()
    tag.layChildren()
    return "<html>" + tag.children.joinToString("") + "</html>"
}

fun HtmlTag.bold(text: String): Stylable {
    return tag("p", text)
}

fun HtmlTag.tag(tag: String, text: String): Stylable {
    val index = children.size
    children.add("<$tag>$text</$tag>")
    return Stylable {
        children[index] = "<$tag style=\"${styles.joinToString("") { "$it;" }}\">$text</$tag>"
    }
}
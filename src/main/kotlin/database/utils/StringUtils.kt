package database.utils

/// <summary>
/// Helpers for delimited string, with support for escaping the delimiter
/// character.
/// </summary>

private const val DELIMITER_STRING = "|"
private const val DELIMITER_CHAR = '|'

private const val ESCAPE_STRING = "~"
private const val ESCAPE_CHAR = '~'

@Deprecated("unused")
fun encodedJoin(vararg strings: String): String {

    return strings.joinToString(DELIMITER_STRING) {
        it.replace(ESCAPE_STRING, ESCAPE_STRING + ESCAPE_STRING)
            .replace(DELIMITER_STRING, ESCAPE_STRING + DELIMITER_STRING)
    }
}

@Deprecated("unused")
fun String.decodedSplit(): List<String> {
    val result = ArrayList<String>()
    var segmentStart = 0
    var i = 0

    while (i < this.length) {
        if (this[i] == ESCAPE_CHAR) {
            i++
        } else if (this[i] == DELIMITER_CHAR) {
            val substr = this.substring(segmentStart, i - segmentStart)
            result.add(unescapeString(substr))
            segmentStart = i + 1
        }

        i++
    }

    val substr = this.substring(segmentStart)
    result.add(substr)

    return result
}

@Deprecated("unused")
private fun unescapeString(string: String) = string
    .replace(ESCAPE_STRING + ESCAPE_STRING, ESCAPE_STRING)
    .replace(ESCAPE_STRING + DELIMITER_STRING, DELIMITER_STRING)

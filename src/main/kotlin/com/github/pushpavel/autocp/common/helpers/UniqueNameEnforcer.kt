package com.github.pushpavel.autocp.common.helpers

import kotlin.math.max

class UniqueNameEnforcer(
    private val nameRegex: Regex,
    val buildName: (prefix: String, suffixNumber: Int) -> String,
    val getCurrentNames: () -> List<String>
) {

    fun buildUniqueNameWithPrefix(preferredPrefix: String): String {
        val currentNames = getCurrentNames()

        var maxSuffixNumber = 0
        currentNames.forEach {
            val matches = nameRegex.matchEntire(it)?.groupValues
            if (matches != null && matches[1] == preferredPrefix) {
                maxSuffixNumber = max(maxSuffixNumber, matches[2].toInt())
            }
        }

        return buildName(preferredPrefix, maxSuffixNumber + 1)
    }

    fun buildUniqueName(preferredName: String): String {
        val currentNames = getCurrentNames()

        if (currentNames.all { it != preferredName })
            return preferredName

        val prefix = nameRegex.matchEntire(preferredName)?.groupValues?.getOrNull(1) ?: preferredName
        return buildUniqueNameWithPrefix(prefix)
    }
}
package tester.diff

import com.intellij.util.text.StringTokenizer
import java.util.function.BiPredicate

abstract class BaseMarker<T>(private val string1: String, private val string2: String) {

    val segments by lazy {
        val segments = ArrayList<MarkedSegment<T>>()
        val tokens1 = StringTokenizer(string1)
        val tokens2 = StringTokenizer(string2)

        var startIndex1 = 0
        var startIndex2 = 0
        var lastMarkType: T? = null

        while (tokens1.hasMoreTokens() || tokens2.hasMoreTokens()) {
            val pos1 = tokens1.currentPosition
            val pos2 = tokens2.currentPosition

            val token1 = if (tokens1.hasMoreTokens()) tokens1.nextToken() else null
            val token2 = if (tokens2.hasMoreTokens()) tokens2.nextToken() else null

            val markType = getMarkType(token1, token2)

            if (lastMarkType == null)
                lastMarkType = markType

            if (markType != lastMarkType) {
                val mark = MarkedSegment(
                    startIndex1, tokens1.currentPosition,
                    startIndex2, tokens2.currentPosition,
                    markType
                )
                segments.add(mark)

                startIndex1 = pos1
                startIndex2 = pos2
                lastMarkType = markType
            }
        }

        if ((startIndex1 != tokens1.currentPosition) || (startIndex2 != tokens2.currentPosition)) {
            val mark = MarkedSegment(
                startIndex1, tokens1.currentPosition,
                startIndex2, tokens2.currentPosition,
                lastMarkType ?: getMarkType(null, null)
            )
            segments.add(mark)
        }

        return@lazy segments
    }

    fun forEachString1Segment(predicate: (String, T) -> Unit) {
        for (segment in segments) {
            val str1 = string1.substring(segment.startIndex1, segment.endIndex1)
            predicate(str1, segment.markType)
        }
    }

    fun forEachString2Segment(predicate: (String, T) -> Unit) {
        for (segment in segments) {
            val str2 = string2.substring(segment.startIndex2, segment.endIndex2)
            predicate(str2, segment.markType)
        }
    }

    abstract fun getMarkType(token1: String?, token2: String?): T

    data class MarkedSegment<T>(
        val startIndex1: Int,
        val endIndex1: Int,
        val startIndex2: Int,
        val endIndex2: Int,
        val markType: T
    )
}


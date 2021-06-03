package tester.diff

class DiffMarker(string1: String, string2: String) : BaseMarker<DiffMarker.DiffType>(string1, string2) {

    enum class DiffType { IDENTICAL, DIFFERENT }

    override fun getMarkType(token1: String?, token2: String?): DiffType {
        return if (token1 == token2)
            DiffType.IDENTICAL
        else
            DiffType.DIFFERENT
    }

}
package database.diff

import dev.pushpavel.autocp.database.Testcase
import dev.pushpavel.autocp.database.TestcaseQueries

@Deprecated("unused")
class TestcaseDiff(testQ: TestcaseQueries) : BaseDiff<Testcase>() {

    private val updater = Updater(testQ)

    override fun isSame(item1: Testcase, item2: Testcase): Boolean {
        return item1.name == item2.name
                && item1.problemName == item1.problemName
                && item1.problemGroup == item2.problemGroup
    }

    override fun isEqual(item1: Testcase, item2: Testcase) = item1 == item2


    fun applyUpdates(from: List<Testcase>, to: List<Testcase>) {
        compute(from, to, updater)
    }

    private class Updater(private val testQ: TestcaseQueries) : Diff.Listener<Testcase> {

        override fun add(item: Testcase) = testQ.insertTestcase(item)

        override fun remove(item: Testcase) =
            testQ.removeTestcase(item.name, item.problemName, item.problemGroup)

        override fun update(oldItem: Testcase, item: Testcase) =
            testQ.updateTestcase(item)
    }
}
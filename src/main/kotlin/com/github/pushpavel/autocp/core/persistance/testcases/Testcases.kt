package com.github.pushpavel.autocp.core.persistance.testcases

import com.github.pushpavel.autocp.core.persistance.base.Table

object Testcases : Table<Testcase.Key, Testcase>() {
    override fun getKey(value: Testcase) = value.getKey()

    // TODO: maintain consistency
}
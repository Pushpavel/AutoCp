package com.github.pushpavel.autocp.core.persistance.solutions

import com.github.pushpavel.autocp.core.persistance.base.Table


object Solutions : Table<String, Solution>() {
    override fun getKey(value: Solution) = value.pathString

    // TODO: maintain consistency
}
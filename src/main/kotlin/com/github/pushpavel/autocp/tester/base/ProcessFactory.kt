package com.github.pushpavel.autocp.tester.base

interface ProcessFactory {
    fun createProcess(): Process
}

class BuildErr(val err: Exception, val command: String) : Exception()
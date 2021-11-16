package com.github.pushpavel.autocp.config.validators

import com.github.pushpavel.autocp.database.models.SolutionFile

sealed class SolutionFilePathErr(val configName: String) : Exception() {
    class BlankPathErr(configName: String) : SolutionFilePathErr(configName)
    class FormatErr(configName: String, val pathString: String) : SolutionFilePathErr(configName)
    class FileDoesNotExist(configName: String, val pathString: String) : SolutionFilePathErr(configName)
    class FileNotRegistered(configName: String, val pathString: String) : SolutionFilePathErr(configName)
}
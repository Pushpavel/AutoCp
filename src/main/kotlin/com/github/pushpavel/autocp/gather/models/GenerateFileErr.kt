package com.github.pushpavel.autocp.gather.models

import com.github.pushpavel.autocp.database.models.Problem

sealed class GenerateFileErr(val problem: Problem, message: String?) : Exception(message) {
    class LangNotConfiguredErr(problem: Problem) : GenerateFileErr(problem, null)
    class FileAlreadyExistsErr(val filePath: String, problem: Problem) : GenerateFileErr(problem, null)
}
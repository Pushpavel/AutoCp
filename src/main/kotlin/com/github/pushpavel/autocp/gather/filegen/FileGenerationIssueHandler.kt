package com.github.pushpavel.autocp.gather.filegen

import com.github.pushpavel.autocp.common.res.R
import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.intellij.util.IncorrectOperationException

class FileGenerationIssueHandler : FileGenerationListener {
    override fun onError(e: Exception, problem: Problem, batch: BatchJson, extension: String) {
        if (e is IncorrectOperationException)
            R.notify.fileGenerationIncorrectOperation(e)
    }
}
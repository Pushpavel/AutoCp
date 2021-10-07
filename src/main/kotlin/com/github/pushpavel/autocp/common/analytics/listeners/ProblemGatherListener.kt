package com.github.pushpavel.autocp.common.analytics.listeners

import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.gather.filegen.FileGenerationListener
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.intellij.openapi.vfs.VirtualFile

class ProblemGatherListener : FileGenerationListener {
    override fun onGenerated(file: VirtualFile, problem: Problem, batch: BatchJson, extension: String) {
//        if (!alreadyGenerated)
//            GoogleAnalytics.instance.sendEvent(ProblemGatheredEvent(problem, lang))
    }
}
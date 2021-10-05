package com.github.pushpavel.autocp.common.analytics.listeners

import com.github.pushpavel.autocp.build.Lang
import com.github.pushpavel.autocp.common.analytics.GoogleAnalytics
import com.github.pushpavel.autocp.common.analytics.events.ProblemGatheredEvent
import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.gather.FileGenerationListener
import com.intellij.openapi.vfs.VirtualFile

class ProblemGatherListener : FileGenerationListener {
    override fun onGenerated(file: VirtualFile, problem: Problem, lang: Lang, alreadyGenerated: Boolean) {
        if (!alreadyGenerated)
            GoogleAnalytics.instance.sendEvent(ProblemGatheredEvent(problem, lang))
    }
}
package com.github.pushpavel.autocp.database.listeners

import com.github.pushpavel.autocp.database.autoCp
import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.gather.base.ProblemGatheringListener
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.intellij.openapi.project.Project

class GatheredProblemAdder(val project: Project) : ProblemGatheringListener {
    override fun onProblemGathered(problems: List<Problem>, batch: BatchJson) {
        project.autoCp().updateProblem(problem = problems.last())
    }
}
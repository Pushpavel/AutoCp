package com.github.pushpavel.autocp.core.persistance.storables.problems

import com.github.pushpavel.autocp.core.persistance.storable
import com.github.pushpavel.autocp.gather.base.ProblemBatchProcessorListener
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.intellij.openapi.project.Project

class GatheredProblemAdder(val project: Project) : ProblemBatchProcessorListener {
    override fun onProblemGathered(problems: List<Problem>, batch: BatchJson) {
        project.storable<Problems>().put(problems.last())
    }
}
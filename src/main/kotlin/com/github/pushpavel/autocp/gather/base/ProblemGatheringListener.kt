package com.github.pushpavel.autocp.gather.base

import com.github.pushpavel.autocp.database.models.Problem
import com.github.pushpavel.autocp.gather.models.BatchJson
import com.intellij.util.messages.Topic

interface ProblemGatheringListener {
    companion object {
        val TOPIC = Topic.create("Problem Gathering Listener", ProblemGatheringListener::class.java)
    }

    fun onBatchStart(problem: Problem, batch: BatchJson) {}
    fun onBatchEnd(e: Exception?, problems: List<Problem>, batch: BatchJson) {}

    fun onProblemGathered(problems: List<Problem>, batch: BatchJson) {}
}
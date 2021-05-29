package com.github.pushpavel.autocp.services

import com.github.pushpavel.autocp.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}

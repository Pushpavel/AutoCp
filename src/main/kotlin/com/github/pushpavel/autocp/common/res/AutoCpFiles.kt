package com.github.pushpavel.autocp.common.res

import com.github.pushpavel.autocp.common.helpers.listPathsInDirectoryInResources

object AutoCpFiles {
    val fileTemplates = listPathsInDirectoryInResources("/fileTemplates/j2ee")
}
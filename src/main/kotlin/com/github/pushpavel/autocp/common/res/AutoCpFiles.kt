package com.github.pushpavel.autocp.common.res

import com.github.pushpavel.autocp.common.helpers.listPathsInDirectoryInResources

object AutoCpFiles {
    val langJsons = listPathsInDirectoryInResources("/languages")
    val fileTemplates = listPathsInDirectoryInResources("/fileTemplates/j2ee")
}
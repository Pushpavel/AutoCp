package common.res

import common.helpers.listPathsInDirectoryInResources

object AutoCpFiles {
    val langJsons = listPathsInDirectoryInResources("/languages")
    val fileTemplates = listPathsInDirectoryInResources("/fileTemplates/j2ee")
}
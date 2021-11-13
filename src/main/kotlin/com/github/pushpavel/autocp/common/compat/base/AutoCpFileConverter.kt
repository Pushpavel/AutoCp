package com.github.pushpavel.autocp.common.compat.base

import java.nio.file.Path

interface AutoCpFileConverter {
    fun convert(autocpFile: Path)
}
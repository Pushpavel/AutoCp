package com.github.pushpavel.autocp.database

import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.vfs.VirtualFile
import com.github.pushpavel.autocp.common.res.R

class AutoCpFileType : FileType {
    override fun getName() = "Autocp Storage File"

    override fun getDefaultExtension() = "autocp"

    override fun getDescription() = "AutoCp storage"

    override fun getIcon() = R.icons.logo16

    override fun isBinary() = false

    override fun getCharset(file: VirtualFile, content: ByteArray?): String? {
        return null
    }
}
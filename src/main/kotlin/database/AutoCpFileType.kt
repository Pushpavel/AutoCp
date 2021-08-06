package database

import com.intellij.openapi.fileTypes.FileType
import res.R

class AutoCpFileType : FileType {
    override fun getName() = "Autocp Storage File"

    override fun getDefaultExtension() = "autocp"

    override fun getDescription() = "AutoCp storage"

    override fun getIcon() = R.icons.logo16

    override fun isBinary() = false
}
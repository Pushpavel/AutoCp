package database

import com.intellij.openapi.fileTypes.FileType
import icons.Icons

class AutoCpFileType : FileType {
    override fun getName() = "Autocp Storage File"

    override fun getDefaultExtension() = "autocp"

    override fun getDescription() = "AutoCp storage"

    override fun getIcon() = Icons.LogoIcon

    override fun isBinary() = false
}
package com.github.pushpavel.autocp.common.compat.autocpFile

import com.github.pushpavel.autocp.common.compat.base.AutoCpFileConverter
import com.google.gson.JsonParser
import com.intellij.util.io.delete
import com.intellij.util.io.readText
import java.nio.file.Path

/**
 * Deletes autocpFile if not in valid json format
 */
class DeleteInvalid : AutoCpFileConverter {
    override fun convert(autocpFile: Path) {
        try {
            JsonParser.parseString(autocpFile.readText())
        } catch (e: Exception) {
            autocpFile.delete()
        }
    }
}
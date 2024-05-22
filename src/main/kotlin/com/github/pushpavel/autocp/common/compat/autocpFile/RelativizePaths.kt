package com.github.pushpavel.autocp.common.compat.autocpFile

import com.github.pushpavel.autocp.common.compat.base.AutoCpFileConverter
import com.github.pushpavel.autocp.common.res.R
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.intellij.openapi.project.Project
import kotlin.io.path.readText
import com.intellij.util.io.write
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.pathString

class RelativizePaths(val project: Project) : AutoCpFileConverter {
    override fun convert(autocpFile: Path) {
        val text = autocpFile.readText()
        val root = JsonParser.parseString(text).asJsonObject
        if (!root.has("version")) {
            root.addProperty("version", R.keys.autoCpFileVersionNumber)
            val solutionFiles = JsonObject()
            val s = root.getAsJsonObject("solutionFiles")
            s.keySet().forEach {
                val p = Path(it)
                val rp = if (p.isAbsolute) Path(project.basePath!!).relativize(p) else p
                val sit = s[it].asJsonObject
                sit.addProperty("pathString", rp.pathString)
                solutionFiles.add(rp.pathString, sit)
            }
            root.add("solutionFiles", solutionFiles)

            autocpFile.write(root.toString())
        }
    }
}
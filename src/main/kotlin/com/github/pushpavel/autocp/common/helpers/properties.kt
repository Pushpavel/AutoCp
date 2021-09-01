package com.github.pushpavel.autocp.common.helpers

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import com.github.pushpavel.autocp.common.res.R

val Project.properties: PropertiesComponent get() = PropertiesComponent.getInstance(this)

var PropertiesComponent.toolWindowSelectedTabIndex
    get() = getInt(R.keys.toolWindowSelectedTabIndexKey, 0)
    set(value) = setValue(R.keys.toolWindowSelectedTabIndexKey, value, 0)

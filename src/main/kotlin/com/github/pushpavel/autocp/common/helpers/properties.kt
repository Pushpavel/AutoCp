package com.github.pushpavel.autocp.common.helpers

import com.github.pushpavel.autocp.common.res.R
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.UUID

val Project.properties: PropertiesComponent get() = PropertiesComponent.getInstance(this)

var PropertiesComponent.toolWindowSelectedTabIndex
    get() = getInt(R.keys.toolWindowSelectedTabIndexKey, 0)
    set(value) = setValue(R.keys.toolWindowSelectedTabIndexKey, value, 0)

val PropertiesComponent.analyticsUserId
    get() = getValue(R.keys.analyticsClientIdKey) ?: UUID.randomUUID().toString().also {
        setValue(R.keys.analyticsClientIdKey, it)
    }


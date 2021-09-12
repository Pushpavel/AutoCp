package com.github.pushpavel.autocp.common.helpers

import com.github.pushpavel.autocp.common.res.R
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.project.Project
import java.util.*
import kotlin.random.Random

val Project.properties: PropertiesComponent get() = PropertiesComponent.getInstance(this)

var PropertiesComponent.toolWindowSelectedTabIndex
    get() = getInt(R.keys.toolWindowSelectedTabIndexKey, 0)
    set(value) = setValue(R.keys.toolWindowSelectedTabIndexKey, value, 0)

val PropertiesComponent.analyticsClientId
    get() = analyticsClientIdOrNull() ?: "${Random.nextLong()}.${Date().time}".also {
        setValue(R.keys.analyticsClientIdKey, it)
    }

fun PropertiesComponent.analyticsClientIdOrNull() = getValue(R.keys.analyticsClientIdKey)

fun PropertiesComponent.unsetAnalyticsClientId() = unsetValue(R.keys.analyticsClientIdKey)



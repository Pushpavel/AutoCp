package com.github.pushpavel.autocp.config.validators

import com.github.pushpavel.autocp.settings.langSettings.model.Lang

class NoBuildConfigErr(val lang: Lang) : Exception()
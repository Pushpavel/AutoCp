package config.validators

import settings.langSettings.model.Lang

class NoBuildConfigErr(val lang: Lang) : Exception()
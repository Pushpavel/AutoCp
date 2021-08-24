package common.errors

import common.res.R

class InternalErr(message: String) : Exception(message)
object NoReachErr : Exception(R.strings.noReachErrMsg)
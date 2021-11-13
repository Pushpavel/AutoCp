package com.github.pushpavel.autocp.common.errors

import com.github.pushpavel.autocp.common.res.R

class InternalErr(message: String) : Exception(message)
object NoReachErr : Exception(R.strings.noReachErrMsg)
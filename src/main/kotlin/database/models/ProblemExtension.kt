package database.models

import com.github.pushpavel.autocp.database.Problem

fun Problem.getTimeLimit(): Long {
    return this.data_.get("timeLimit").asLong
}
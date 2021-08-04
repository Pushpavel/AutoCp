package database.models

import com.github.pushpavel.autocp.database.Problem

@Deprecated("use database.models.Problem")
fun Problem.getTimeLimit(): Long {
    return this.data_.get("timeLimit").asLong
}
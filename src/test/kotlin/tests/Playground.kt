package tests

import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class Playground {

    @Test
    fun play() = runBlocking {
        val job = Job()
        job.cancel()
        job.cancel()
        job.cancel()
        job.cancel()
    }

}
package worker

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class Scheduler(private val task: Runnable) {
    private val executor = Executors.newScheduledThreadPool(1)

    fun scheduleExecution(every: Every) {

        val taskWrapper = Runnable {
            task.run()
        }

        executor.scheduleWithFixedDelay(taskWrapper, every.n, every.n, every.unit)
    }
}

data class Every(val n: Long, val unit: TimeUnit)
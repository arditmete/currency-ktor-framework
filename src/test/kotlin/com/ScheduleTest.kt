package com

import junit.framework.Assert.assertTrue
import org.junit.Test
import worker.Every
import worker.Scheduler
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class SchedulerTest {
    @Test
    fun `scheduleExecution executes the task periodically`() {
        val countDownLatch = CountDownLatch(3) // The task should run 3 times
        val scheduler = Scheduler(Runnable {
            countDownLatch.countDown()
        })
        val every = Every(1, TimeUnit.SECONDS)

        scheduler.scheduleExecution(every)

        assertTrue(countDownLatch.await(5, TimeUnit.SECONDS)) // Wait for the task to run 3 times within 5 seconds
    }
}
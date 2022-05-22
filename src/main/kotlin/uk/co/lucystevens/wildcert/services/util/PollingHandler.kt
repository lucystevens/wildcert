package uk.co.lucystevens.wildcert.services.util

import org.shredzone.acme4j.Status
import org.shredzone.acme4j.exception.AcmeRetryAfterException
import uk.co.lucystevens.wildcert.config.Config
import java.time.Clock

class PollingHandler(
    private val clock: Clock,
    private val config: Config
    ) {

    fun waitForComplete(getStatus: () -> Status, update: () -> Unit): Long{
        return waitFor(listOf(Status.VALID, Status.INVALID), getStatus, update)
    }

    fun waitForReady(getStatus: () -> Status, update: () -> Unit): Long{
        return waitFor(listOf(Status.READY), getStatus, update)
    }

    fun waitFor(expectedStatuses: List<Status>, getStatus: () -> Status, update: () -> Unit): Long{
        val startTime = clock.millis()
        var retryAfter = startTime + config.getPollingRetryIntervalMs()
        val timeoutTime = startTime + config.getPollingTimeoutMs()
        while (!expectedStatuses.contains(getStatus()) && clock.millis() < timeoutTime) {
            Thread.sleep(config.getPollingWaitTimeMs())

            if(clock.millis() > retryAfter)
                retryAfter = doUpdate(update)
        }

        // TODO throw exception if not valid
        return clock.millis() - startTime
    }

    private fun doUpdate(update: () -> Unit): Long =
        try {
            update()
            clock.millis() + config.getPollingRetryIntervalMs()
        } catch(e: AcmeRetryAfterException) {
            e.retryAfter.toEpochMilli()
        }
}
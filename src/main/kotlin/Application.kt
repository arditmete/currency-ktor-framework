import io.ktor.server.engine.*
import io.ktor.server.netty.*
import config.plugins
import config.routing
import dependencies.Dependencies
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import worker.Every
import worker.Scheduler
import java.util.concurrent.TimeUnit

@OptIn(DelicateCoroutinesApi::class)
fun main() {
    with(Dependencies()) {
        Scheduler {
            GlobalScope.launch {
                Dependencies().converterService.addCurrencyToRedis("USD")
                Dependencies().converterService.addCurrencyToRedis("EUR")
                Dependencies().converterService.addCurrencyToRedis("ALL")
            }
        }.scheduleExecution(Every(10, TimeUnit.HOURS))
        embeddedServer(Netty, port = 8080) {
            plugins()
            routing(this@with)
        }.start(wait = true)
    }
}

package config

import dependencies.Dependencies
import io.ktor.application.*
import io.ktor.routing.*
import routes.convertFromTo

fun Application.routing(dependencies: Dependencies) {
    routing {
        convertFromTo(dependencies.currencyController, dependencies.converterService)
    }
}

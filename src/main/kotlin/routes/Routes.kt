package routes

import controller.CurrencyController
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import service.ConverterService

fun Route.convertFromTo(currencyController: CurrencyController, converterService: ConverterService) {
    get("/convert") {
        val params = call.request.queryParameters
        converterService.validateParams(params)
        call.respond(currencyController.convertFromTo(params["from"]!!, params["to"]!!, params["amount"]!!))
    }
}
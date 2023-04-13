package config

import validators.ErrorCode
import validators.IncorrectEndpointException
import validators.InternalServerException
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import model.UnsuccessfulConverterResponse
import org.slf4j.event.Level

fun Application.plugins() {
    install(StatusPages) {
        exception<Exception> { cause ->
            when (cause) {
                is InternalServerException -> {
                    val response = UnsuccessfulConverterResponse(
                        errorMessage = "${cause.message}",
                        error = ErrorCode.INTERNAL_SERVER_ERROR.name,
                        statusCode = ErrorCode.INTERNAL_SERVER_ERROR.code
                    )
                    call.respond(HttpStatusCode.InternalServerError, response)
                    log.info("InternalServerException: ${cause.message}")
                }
                is IncorrectEndpointException -> {
                    val response = UnsuccessfulConverterResponse(
                        errorMessage = "Bad request to remote server!",
                        details = "${cause.message}",
                        error = ErrorCode.INCORRECT_REQUEST_BODY.name,
                        statusCode = ErrorCode.INCORRECT_REQUEST_BODY.code
                    )
                    call.respond(HttpStatusCode.BadRequest, response)
                    log.info("RemoteException: ${cause.message}")
                }
            }
        }
    }

    install(ContentNegotiation) {
        jackson()
    }

    install(CallLogging) {
        level = Level.INFO
    }
}

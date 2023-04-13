package validators

object ErrorCode {
    var INCORRECT_REQUEST_BODY = ErrorObject("INCORRECT_REQUEST_BODY", "400")
    var INTERNAL_SERVER_ERROR = ErrorObject("INTERNAL_SERVER_ERROR", "500")
}


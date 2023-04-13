package model

import java.math.BigDecimal

interface Response

data class SuccessfulConverterResponse(
    val currencyFrom: String,
    val currencyTo: String,
    val rate: String,
    val totalAmount: BigDecimal,
) : Response

data class UnsuccessfulConverterResponse(
    val error: String,
    val errorMessage: String,
    val details: String? = null,
    val statusCode: String
) : Response
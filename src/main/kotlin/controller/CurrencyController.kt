package controller

import model.Response
import service.ConverterService
import validators.Validator

class CurrencyController(private val converterService: ConverterService, private val validator: Validator) {
    suspend fun convertFromTo(fromCurrency: String, toCurrency: String, amount: String): Response {
        return converterService.convert(fromCurrency, toCurrency, amount, validator)
    }
}
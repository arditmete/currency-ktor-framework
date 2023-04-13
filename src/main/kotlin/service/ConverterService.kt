package service

import com.google.gson.Gson
import config.Config
import validators.IncorrectEndpointException
import validators.UnavailableServiceException
import validators.ValidatorException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import model.ApiResponse
import model.Currency
import model.SuccessfulConverterResponse
import org.json.JSONObject
import redis.clients.jedis.Jedis
import validators.Validator
import java.math.BigDecimal

open class ConverterService(
    private val httpClient: HttpClient = HttpClient(),
    private val config: Config,
    private var jedis: Jedis
) {
    init {
        jedis = Jedis("localhost", 6379)
    }

    private val gson = Gson()

    open suspend fun addCurrencyToRedis(fromCurrency: String, toCurrency: String? = null) {
        val url = config.api +
                "source=$fromCurrency"
        try {
            getAsJson(url, "$fromCurrency$toCurrency")
        } catch (e: org.json.JSONException) {
            throw UnavailableServiceException(e.toString())
        } catch (e: NumberFormatException) {
            throw IncorrectEndpointException(e.toString())
        }
    }


    fun findOne(id: String): Currency? {
        val value = jedis.get(id)
        return value?.let { Currency(id, it) }
    }

    fun saveToRedis(quotes: Map<String, Double>?, currency: String) {
        val currencyList = mutableListOf<Currency>()
        if (quotes != null) {
            checkCurrency(currency)
            for (quote in quotes) {
                currencyList.add(Currency(quote.key, quote.value.toString()))
                jedis.set(quote.key, quote.value.toString())
            }
        }
    }

    fun checkCurrency(currency: String?) {
        if (jedis.get(currency) == null) {
            throw ValidatorException("Currency converter $currency not  found!")
        }

    }

     suspend fun getAsJson(url: String, currency: String) {
        try {
            val httpResponse: HttpResponse = httpClient.get(url) {
                headers {
                    append("apikey", config.apiKey)
                }
            }
            val responseBody: ByteArray = httpResponse.receive()
            val json = JSONObject(responseBody.toString(Charsets.UTF_8))
            val apiResponse = gson.fromJson(json.toString(), ApiResponse::class.java)
            saveToRedis(apiResponse.quotes, currency)
        } catch (e: Exception) {
            throw IncorrectEndpointException(e.toString())
        }
    }

    fun calculate(fromCurrency: String, toCurrency: String, amount: String): BigDecimal {
        val currencyConversion = findOne(fromCurrency + toCurrency)
        return amount.toBigDecimal().multiply(currencyConversion?.value?.toBigDecimal())
    }

    fun validateParams(params: Parameters) {
        if (params["from"] == null) {
            throw ValidatorException("Field from is missing!")
        } else if (params.get("to") == null) {
            throw ValidatorException("Field to is missing!")
        } else if (params.get("amount") == null) {
            throw ValidatorException("Field amount is missing!")
        }
    }

    suspend fun convert(
        fromCurrency: String,
        toCurrency: String,
        amount: String,
        validator: Validator,
    ): SuccessfulConverterResponse {
        var currency = findOne("$fromCurrency$toCurrency")
        if (currency == null)
            addCurrencyToRedis(fromCurrency, toCurrency)
        currency = findOne(fromCurrency + toCurrency)
        validator.validateConvertFromTo(fromCurrency, toCurrency, currency!!.name)
        val totalAmount = calculate(fromCurrency, toCurrency, amount)
        return SuccessfulConverterResponse(
            fromCurrency,
            toCurrency,
            currency.value,
            totalAmount
        )
    }

}



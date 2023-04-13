import com.google.gson.Gson
import config.Config
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import model.ApiResponse
import model.SuccessfulConverterResponse
import org.json.JSONObject
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever
import redis.clients.jedis.Jedis
import service.ConverterService
import validators.UnavailableServiceException
import validators.Validator
import validators.ValidatorException
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ConverterServiceTest {

    @Mock
    var jedis: Jedis = Jedis("localhost", 6379)

    @Mock
    var validator: Validator = Validator()

    @Mock
    var httpClient: HttpClient = HttpClient()

    @Test
    fun `findOne should return the currency if it exists in Redis`() {
        val converterService = ConverterService(
            jedis = jedis,
            config = Config("Ja28GjJrGIIcwXs9jGAEkwdMJJdeyjaX", "https://api.apilayer.com/currency_data/live?")
        )
        val currency = model.Currency("USDEUR", "0.90511")
        val result = converterService.findOne("USDEUR")
        assertEquals(currency.name, result?.name)
    }

    @Test
    fun `findOne should return null if the currency does not exist in Redis`() {
        val converterService = ConverterService(
            jedis = jedis,
            config = Config("Ja28GjJrGIIcwXs9jGAEkwdMJJdeyjaX", "https://api.apilayer.com/currency_data/live?")
        )
        val result = converterService.findOne("EURUSA")
        assertEquals(null, result)
    }

    @Test
    fun `test valid parameters`() {
        val converterService = ConverterService(
            jedis = jedis,
            config = Config("Ja28GjJrGIIcwXs9jGAEkwdMJJdeyjaX", "https://api.apilayer.com/currency_data/live?")
        )
        val params: Map<String, List<String>> = mapOf(
            "from" to listOf("John"),
            "to" to listOf("Alice"),
            "amount" to listOf("100")
        )
        assertDoesNotThrow { converterService.validateParams(params) }
    }
//
//    @Test
//    fun `addCurrencyToRedis should call getAsJson with correct URL`() {
//        val converterService = ConverterService(
//            jedis = jedis,
//            config = Config("Ja28GjJrGIIcwXs9jGAEkwdMJJdeyjaX", "https://api.apilayer.com/currency_data/live?")
//        )
//        val fromCurrency = "USD"
//        val expectedUrl = "https://api.apilayer.com/currency_data/live?$fromCurrency"
//        runBlocking {
//            converterService.addCurrencyToRedis(fromCurrency)
//            verify(converterService).getAsJson(eq(expectedUrl), eq("USDnull"))
//        }
//    }

//    @Test
//    fun `addCurrencyToRedis should call getAsJson with correct URL and toCurrency`() {
//        val converterService = ConverterService(
//            jedis = jedis,
//            config = Config("Ja28GjJrGIIcwXs9jGAEkwdMJJdeyjaX", "https://api.apilayer.com/currency_data/live?")
//        )
//        val fromCurrency = "USD"
//        val toCurrency = "EUR"
//        val expectedUrl = "https://api.apilayer.com/currency_data/live?$fromCurrency"
//        runBlocking {
//
//            converterService.addCurrencyToRedis(fromCurrency, toCurrency)
//
//            verify(converterService).getAsJson(eq(expectedUrl), eq("USDEUR"))
//        }
//    }

//    @Test
//    suspend fun `addCurrencyToRedis should throw UnavailableServiceException when JSONException is thrown`() {
//        val converterService = ConverterService(
//            jedis = jedis,
//            config = Config("Ja28GjJrGIIcwXs9jGAEkwdMJJdeyjaX", "https://api.apilayer.com/currency_data/live?")
//        )
//        val fromCurrency = "USD"
//        val expectedUrl = "https://api.apilayer.com/currency_data/live?$fromCurrency"
//        val exception = org.json.JSONException("Invalid JSON")
//        runBlocking {
//            doThrow(exception).`when`(converterService).getAsJson(eq(expectedUrl), any())
//            assertThrows<UnavailableServiceException> {
//                converterService.addCurrencyToRedis(fromCurrency)
//            }
//        }
//    }

    @Test
    fun `test missing from`() {
        val converterService = ConverterService(
            jedis = jedis,
            config = Config("Ja28GjJrGIIcwXs9jGAEkwdMJJdeyjaX", "https://api.apilayer.com/currency_data/live?")
        )
        val params = mapOf(
            "to" to listOf("Alice"),
            "amount" to listOf("100")
        )
        val exception = assertThrows(ValidatorException::class.java) { converterService.validateParams(params) }
        assertEquals("Field from is missing!", exception.message)
    }

    @Test
    fun `test missing to`() {
        val converterService = ConverterService(
            jedis = jedis,
            config = Config("Ja28GjJrGIIcwXs9jGAEkwdMJJdeyjaX", "https://api.apilayer.com/currency_data/live?")
        )
        val params = mapOf(
            "from" to listOf("John"),
            "amount" to listOf("100")
        )
        val exception = assertThrows(ValidatorException::class.java) { converterService.validateParams(params) }
        assertEquals("Field to is missing!", exception.message)
    }

    @Test
    fun `test missing amount`() {
        val converterService = ConverterService(
            jedis = jedis,
            config = Config("Ja28GjJrGIIcwXs9jGAEkwdMJJdeyjaX", "https://api.apilayer.com/currency_data/live?")
        )
        val params = mapOf(
            "from" to listOf("John"),
            "to" to listOf("Alice"),
        )
        val exception = assertThrows(ValidatorException::class.java) { converterService.validateParams(params) }
        assertEquals("Field amount is missing!", exception.message)
    }

    @Test
    fun `saveToRedis should save the quotes to Redis`() {
        val converterService = ConverterService(
            jedis = jedis,
            config = Config("Ja28GjJrGIIcwXs9jGAEkwdMJJdeyjaX", "https://api.apilayer.com/currency_data/live?")
        )
        val quotes = mapOf("EURUSD" to 1.0, "USDEUR" to 0.9)
        val currencyList = listOf(model.Currency("EURUSD", "1.0"), model.Currency("USDEUR", "0.9"))
        converterService.saveToRedis(quotes, "USDEUR")
        val currencySaved = converterService.findOne("USDEUR")
        assertEquals(currencySaved, currencyList.find { it.name == "USDEUR" })
    }

    @Test
    fun `checkCurrency should throw ValidatorException if the currency is not found in Redis`() {
        val converterService = ConverterService(
            jedis = jedis,
            config = Config("Ja28GjJrGIIcwXs9jGAEkwdMJJdeyjaX", "https://api.apilayer.com/currency_data/live?")
        )
        assertFailsWith<ValidatorException> {
            converterService.checkCurrency("JPY")
        }
    }
}
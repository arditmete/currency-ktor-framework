import config.Config
import controller.CurrencyController
import kotlinx.coroutines.runBlocking
import model.SuccessfulConverterResponse
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mock
import redis.clients.jedis.Jedis
import service.ConverterService
import validators.Validator
import java.math.BigDecimal

class CurrencyControllerTest {
    @Mock
    var jedis: Jedis = Jedis("localhost", 6379)

    @Mock
    var converterService: ConverterService = ConverterService(jedis = jedis,
        config = Config("Ja28GjJrGIIcwXs9jGAEkwdMJJdeyjaX", "https://api.apilayer.com/currency_data/live?")
    )

    @Mock
    var validator: Validator = Validator()

    @Mock
    var currencyController: CurrencyController = CurrencyController(converterService, validator)

    @Test
    fun `convertFromTo should return the response from converterService`() = runBlocking {
        val fromCurrency = "USD"
        val toCurrency = "EUR"
        val amount = "100"
        val expectedResponse = SuccessfulConverterResponse("USD", "EUR", "0.9", BigDecimal(90.0))

        val actualResponse: SuccessfulConverterResponse =
            currencyController.convertFromTo(fromCurrency, toCurrency, amount) as SuccessfulConverterResponse

        assertEquals(expectedResponse.totalAmount.toLong(), actualResponse.totalAmount.toLong())
    }
}
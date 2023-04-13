import config.Config
import config.routing
import controller.CurrencyController
import dependencies.Dependencies
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.routing.*
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import redis.clients.jedis.Jedis
import routes.convertFromTo
import service.ConverterService
import validators.Validator

@RunWith(MockitoJUnitRunner::class)
class ApplicationTest {


    @Mock
    var jedis: Jedis = Jedis("localhost", 6379)

    @Mock
    var validator: Validator = Validator()


    @Mock
    var converterService: ConverterService = ConverterService(HttpClient(), Config("Ja28GjJrGIIcwXs9jGAEkwdMJJdeyjaX", "https://api.apilayer.com/currency_data/live?"), jedis)

    @Mock
     var currencyController: CurrencyController = CurrencyController(converterService, validator)

    @Mock
    var dependencies: Dependencies = Dependencies(config = Config("Ja28GjJrGIIcwXs9jGAEkwdMJJdeyjaX", "https://api.apilayer.com/currency_data/live?"), validator, jedis, converterService, currencyController)
    @Mock
    lateinit var routing: Routing

    @Mock
    lateinit var application: Application

    @Test
    fun `routing should call convertFromTo with dependencies`() {
        // Arrange
        `when`(dependencies.currencyController).thenReturn(currencyController)
        `when`(dependencies.converterService).thenReturn(converterService)

        // Acta
        application.routing(dependencies)

        // Assert
        verify { routing.convertFromTo(currencyController, converterService) }
    }
}
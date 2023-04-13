import model.SuccessfulConverterResponse
import model.UnsuccessfulConverterResponse
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class ConverterResponseTest {
    @Test
    fun testSuccessfulConverterResponse() {
        val response = SuccessfulConverterResponse(
            "USD", "INR", "73.702504", BigDecimal("100.00")
        )
        assertEquals("USD", response.currencyFrom)
        assertEquals("INR", response.currencyTo)
        assertEquals("73.702504", response.rate)
        assertEquals(BigDecimal("100.00"), response.totalAmount)
    }

    @Test
    fun testUnsuccessfulConverterResponse() {
        val response = UnsuccessfulConverterResponse(
            "invalid_request_error", "Invalid request", statusCode = "400"
        )
        assertEquals("invalid_request_error", response.error)
        assertEquals("Invalid request", response.errorMessage)
        assertEquals(null, response.details)
        assertEquals("400", response.statusCode)
    }
}
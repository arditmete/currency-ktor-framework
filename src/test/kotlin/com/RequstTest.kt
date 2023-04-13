import model.Request
import org.junit.Assert.assertEquals
import org.junit.Test

class RequestTest {
    @Test
    fun testRequest() {
        val request = Request("USD", "INR", "1.00")
        assertEquals("USD", request.from)
        assertEquals("INR", request.to)
        assertEquals("1.00", request.currency)
    }
}
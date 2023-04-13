import model.Currency
import org.junit.Test
import kotlin.test.assertEquals

class CurrencyTest {
    @Test
    fun testCurrency() {
        val currency = Currency("USD", "1.00")
        assertEquals("USD", currency.name)
        assertEquals("1.00", currency.value)
    }
}
import org.junit.Assert.assertThrows
import org.junit.Test
import org.mockito.Mockito.*
import validators.Validator
import validators.ValidatorException

class ValidatorTest {
    @Test
    fun `validateConvertFromTo throws an exception if fromCurrency is null`() {
        val validator = spy(Validator())
        val toCurrency = "USD"
        val correctCurrencies = "USD,EUR,GBP"
        assertThrows(ValidatorException::class.java) {
            validator.validateConvertFromTo(null, toCurrency, correctCurrencies)
        }
        verify(validator).validateConvertFromTo(null, toCurrency, correctCurrencies)
    }

    @Test
    fun `validateConvertFromTo throws an exception if fromCurrency is not valid`() {
        val validator = spy(Validator())
        val fromCurrency = "invalid"
        val toCurrency = "USD"
        val correctCurrencies = "USD,EUR,GBP"

        assertThrows(ValidatorException::class.java) {
            validator.validateConvertFromTo(fromCurrency, toCurrency, correctCurrencies)
        }
        verify(validator).validateConvertFromTo(fromCurrency, toCurrency, correctCurrencies)
    }

    @Test
    fun `validateConvertFromTo throws an exception if toCurrency is null`() {
        val validator = spy(Validator())
        val fromCurrency = "USD"
        val correctCurrencies = "USD,EUR,GBP"
        assertThrows(ValidatorException::class.java) {
            validator.validateConvertFromTo(fromCurrency, null, correctCurrencies)
        }
        verify(validator).validateConvertFromTo(fromCurrency, null, correctCurrencies)
    }

    @Test
    fun `validateConvertFromTo throws an exception if toCurrency is not valid`() {
        val validator = spy(Validator())
        val fromCurrency = "USD"
        val toCurrency = "invalid"
        val correctCurrencies = "USD,EUR,GBP"
        assertThrows(ValidatorException::class.java) {
            validator.validateConvertFromTo(fromCurrency, toCurrency, correctCurrencies)
        }
        verify(validator).validateConvertFromTo(fromCurrency, toCurrency, correctCurrencies)
    }

    @Test
    fun `validateConvertFromTo does not throw an exception if the parameters are valid`() {
        val validator = spy(Validator())
        val fromCurrency = "USD"
        val toCurrency = "EUR"
        val correctCurrencies = "USD,EUR,GBP"
        validator.validateConvertFromTo(fromCurrency, toCurrency, correctCurrencies)
        verify(validator).validateConvertFromTo(fromCurrency, toCurrency, correctCurrencies)
    }
}
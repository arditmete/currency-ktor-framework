import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import validators.ErrorCode
import validators.ErrorObject

class ErrorCodeTest {
    @Test
    fun `test INCORRECT_REQUEST_BODY`() {
        val expected = ErrorObject("INCORRECT_REQUEST_BODY", "400")
        val actual = ErrorCode.INCORRECT_REQUEST_BODY
        assertEquals(expected, actual)
    }

    @Test
    fun `test INTERNAL_SERVER_ERROR`() {
        val expected = ErrorObject("INTERNAL_SERVER_ERROR", "500")
        val actual = ErrorCode.INTERNAL_SERVER_ERROR
        assertEquals(expected, actual)
    }
}
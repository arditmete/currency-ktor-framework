import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import validators.ErrorObject

class ErrorObjectTest {
    @Test
    fun `test name`() {
        val name = "Error"
        val error = ErrorObject(name, "500")
        assertEquals(name, error.name)
    }

    @Test
    fun `test code`() {
        val code = "500"
        val error = ErrorObject("Error", code)
        assertEquals(code, error.code)
    }

    @Test
    fun `test equality`() {
        val error1 = ErrorObject("Error", "500")
        val error2 = ErrorObject("Error", "500")
        assertEquals(error1, error2)
    }

    @Test
    fun `test inequality`() {
        val error1 = ErrorObject("Error", "500")
        val error2 = ErrorObject("Error", "404")
        assertNotEquals(error1, error2)
    }
}
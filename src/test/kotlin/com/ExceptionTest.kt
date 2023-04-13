import org.junit.Test
import org.junit.Assert.*
import validators.IncorrectEndpointException
import validators.InternalServerException
import validators.RemoteException
import validators.UnavailableServiceException
import validators.ValidatorException

class ExceptionsTest {

    @Test
    fun testValidatorException() {
        val message = "Invalid input data"
        val exception = ValidatorException(message)
        assertEquals(message, exception.message)
    }

    @Test
    fun testUnavailableServiceException() {
        val message = "Service is currently unavailable"
        val exception = UnavailableServiceException(message)
        assertEquals(message, exception.message)
    }

    @Test
    fun testIncorrectEndpointException() {
        val message = "Endpoint is not valid"
        val exception = IncorrectEndpointException(message)
        assertEquals(message, exception.message)
    }

    @Test
    fun testValidatorExceptionIsInternalServerException() {
        val message = "Invalid input data"
        val exception: InternalServerException = ValidatorException(message)
        assertEquals(message, exception.message)
    }

    @Test
    fun testUnavailableServiceExceptionIsRemoteException() {
        val message = "Service is currently unavailable"
        val exception: RemoteException = UnavailableServiceException(message)
        assertEquals(message, exception.message)
    }

    @Test
    fun testIncorrectEndpointExceptionIsRemoteException() {
        val message = "Endpoint is not valid"
        val exception: RemoteException = IncorrectEndpointException(message)
        assertEquals(message, exception.message)
    }
}

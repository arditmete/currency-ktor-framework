import com.google.gson.Gson
import model.ApiResponse
import org.junit.Test
import kotlin.test.assertEquals

class ApiResponseTest {
    @Test
    fun testApiResponseDeserialization() {
        val jsonString =
            "{\"quotes\":{\"USDINR\":73.702504},\"source\":\"USD\",\"success\":true,\"timestamp\":1649899200}"
        val gson = Gson()
        val apiResponse = gson.fromJson(jsonString, ApiResponse::class.java)
        assertEquals(mapOf("USDINR" to 73.702504), apiResponse.quotes)
        assertEquals("USD", apiResponse.source)
        assertEquals(true, apiResponse.success)
        assertEquals(1649899200, apiResponse.timestamp)
    }
}
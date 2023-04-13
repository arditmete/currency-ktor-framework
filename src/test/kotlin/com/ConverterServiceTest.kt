import com.google.gson.Gson
import config.Config
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import model.ApiResponse
import org.json.JSONObject
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import redis.clients.jedis.Jedis
import service.ConverterService
import validators.ValidatorException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ConverterServiceTest {

    @Mock
    var jedis: Jedis = Jedis("localhost", 6379)

    @Mock
    var httpClient: HttpClient = HttpClient()

    @Test
    fun `findOne should return the currency if it exists in Redis`() {
        val converterService = ConverterService(
            jedis = jedis,
            config = Config("Ja28GjJrGIIcwXs9jGAEkwdMJJdeyjaX", "https://api.apilayer.com/currency_data/live?")
        )
        val currency = model.Currency("USDEUR", "0.90765")
        `when`(jedis.get("USDEUR")).thenReturn("1.0")
        val result = converterService.findOne("USDEUR")
        assertEquals(currency, result)
    }

    @Test
    fun `findOne should return null if the currency does not exist in Redis`() {
        val converterService = ConverterService(
            jedis = jedis,
            config = Config("Ja28GjJrGIIcwXs9jGAEkwdMJJdeyjaX", "https://api.apilayer.com/currency_data/live?")
        )
        val result = converterService.findOne("EURUSA")
        assertEquals(null, result)
    }

    @Test
    fun `saveToRedis should save the quotes to Redis`() {
        val converterService = ConverterService(
            jedis = jedis,
            config = Config("Ja28GjJrGIIcwXs9jGAEkwdMJJdeyjaX", "https://api.apilayer.com/currency_data/live?")
        )
        val quotes = mapOf("EURUSD" to 1.0, "USDEUR" to 0.9)
        val currencyList = listOf(model.Currency("EURUSD", "1.0"), model.Currency("USDEUR", "0.9"))
        converterService.saveToRedis(quotes, "USDEUR")
        val currencySaved = converterService.findOne("USDEUR")
        assertEquals(currencySaved, currencyList.find { it.name == "USDEUR" })
    }

    @Test
    fun `checkCurrency should throw ValidatorException if the currency is not found in Redis`() {
        val converterService = ConverterService(
            jedis = jedis,
            config = Config("Ja28GjJrGIIcwXs9jGAEkwdMJJdeyjaX", "https://api.apilayer.com/currency_data/live?")
        )
        assertFailsWith<ValidatorException> {
            converterService.checkCurrency("JPY")
        }
    }

//    @Test
//    fun `getAsJson should call the client and save the data to database `() {
//        val converterService = ConverterService(
//            jedis = jedis,
//            config = Config("Ja28GjJrGIIcwXs9jGAEkwdMJJdeyjaX", "https://api.apilayer.com/currency_data/live?")
//        )
//        // Set up the mock Jedis object to return a value when get() is called
//        val expectedValue = "0.85"
//        `when`(jedis.get("USDGBP")).thenReturn(expectedValue)
//
//        // Call the getAsJson method and check that the result is as expected
//        val currency = "USDGBP"
//        converterService.getAsJson("https://api.apilayer.com/currency_data/live?", currency)
//        verify(jedis).set("GBP", expectedValue)
//    }

    @Test
    fun testGetAsJson() {
        val converterService = ConverterService(
            jedis = jedis,
            config = Config("Ja28GjJrGIIcwXs9jGAEkwdMJJdeyjaX", "https://api.apilayer.com/currency_data/live?")
        )
        val currency = "GBP"
        val url = "https://api.apilayer.com/currency_data/live?source=GBP"

        var apiResponse = ApiResponse()
        val expectedJson = "[ \"quotes\": {\n" +
                "        \"GBPAED\": 4.599129,\n" +
                "        \"GBPAFN\": 108.950794,\n" +
                "        \"GBPALL\": 129.302353,\n" +
                "        \"GBPAMD\": 485.861085,\n" +
                "        \"GBPANG\": 2.256651,\n" +
                "        \"GBPAOA\": 636.802953,\n" +
                "        \"GBPARS\": 268.839855,\n" +
                "        \"GBPAUD\": 1.850622,\n" +
                "        \"GBPAWG\": 2.2573,\n" +
                "        \"GBPAZN\": 2.125306,\n" +
                "        \"GBPBAM\": 2.223434,\n" +
                "        \"GBPBBD\": 2.528221,\n" +
                "        \"GBPBDT\": 133.249173,\n" +
                "        \"GBPBGN\": 2.213194,\n" +
                "        \"GBPBHD\": 0.472159,\n" +
                "        \"GBPBIF\": 2601.060323,\n" +
                "        \"GBPBMD\": 1.252316,\n" +
                "        \"GBPBND\": 1.659349,\n" +
                "        \"GBPBOB\": 8.652499,\n" +
                "        \"GBPBRL\": 6.150244,\n" +
                "        \"GBPBSD\": 1.252128,\n" +
                "        \"GBPBTC\": 4.1374383e-05,\n" +
                "        \"GBPBTN\": 102.497636,\n" +
                "        \"GBPBWP\": 16.453654,\n" +
                "        \"GBPBYN\": 3.161215,\n" +
                "        \"GBPBYR\": 24545.393513,\n" +
                "        \"GBPBZD\": 2.523924,\n" +
                "        \"GBPCAD\": 1.67481,\n" +
                "        \"GBPCDF\": 2563.491216,\n" +
                "        \"GBPCHF\": 1.11237,\n" +
                "        \"GBPCLF\": 0.036223,\n" +
                "        \"GBPCLP\": 999.536023,\n" +
                "        \"GBPCNY\": 8.602914,\n" +
                "        \"GBPCOP\": 5534.372602,\n" +
                "        \"GBPCRC\": 671.077441,\n" +
                "        \"GBPCUC\": 1.252316,\n" +
                "        \"GBPCUP\": 33.186374,\n" +
                "        \"GBPCVE\": 126.014336,\n" +
                "        \"GBPCZK\": 26.358369,\n" +
                "        \"GBPDJF\": 222.947978,\n" +
                "        \"GBPDKK\": 8.438233,\n" +
                "        \"GBPDOP\": 68.6896,\n" +
                "        \"GBPDZD\": 169.195376,\n" +
                "        \"GBPEGP\": 38.689674,\n" +
                "        \"GBPERN\": 18.78474,\n" +
                "        \"GBPETB\": 67.625242,\n" +
                "        \"GBPEUR\": 1.132494,\n" +
                "        \"GBPFJD\": 2.782018,\n" +
                "        \"GBPFKP\": 1.004419,\n" +
                "        \"GBPGEL\": 3.155757,\n" +
                "        \"GBPGGP\": 1.004419,\n" +
                "        \"GBPGHS\": 13.994612,\n" +
                "        \"GBPGIP\": 1.004419,\n" +
                "        \"GBPGMD\": 77.895987,\n" +
                "        \"GBPGNF\": 10838.795406,\n" +
                "        \"GBPGTQ\": 9.763282,\n" +
                "        \"GBPGYD\": 264.827189,\n" +
                "        \"GBPHKD\": 9.830618,\n" +
                "        \"GBPHNL\": 30.832627,\n" +
                "        \"GBPHRK\": 8.52924,\n" +
                "        \"GBPHTG\": 194.708583,\n" +
                "        \"GBPHUF\": 422.744116,\n" +
                "        \"GBPIDR\": 18446.301536,\n" +
                "        \"GBPILS\": 4.569539,\n" +
                "        \"GBPIMP\": 1.004419,\n" +
                "        \"GBPINR\": 102.261557,\n" +
                "        \"GBPIQD\": 1828.381354,\n" +
                "        \"GBPIRR\": 52941.658596,\n" +
                "        \"GBPISK\": 168.850298,\n" +
                "        \"GBPJEP\": 1.004419,\n" +
                "        \"GBPJMD\": 190.399916,\n" +
                "        \"GBPJOD\": 0.889077,\n" +
                "        \"GBPJPY\": 165.710179,\n" +
                "        \"GBPKES\": 168.310382,\n" +
                "        \"GBPKGS\": 109.594929,\n" +
                "        \"GBPKHR\": 5090.664557,\n" +
                "        \"GBPKMF\": 580.104101,\n" +
                "        \"GBPKPW\": 1127.110672,\n" +
                "        \"GBPKRW\": 1632.130385,\n" +
                "        \"GBPKWD\": 0.383059,\n" +
                "        \"GBPKYD\": 1.043516,\n" +
                "        \"GBPKZT\": 564.731061,\n" +
                "        \"GBPLAK\": 21546.097224,\n" +
                "        \"GBPLBP\": 103522.701712,\n" +
                "        \"GBPLKR\": 404.476779,\n" +
                "        \"GBPLRD\": 203.908394,\n" +
                "        \"GBPLSL\": 23.054818,\n" +
                "        \"GBPLTL\": 3.697764,\n" +
                "        \"GBPLVL\": 0.757513,\n" +
                "        \"GBPLYD\": 5.973921,\n" +
                "        \"GBPMAD\": 12.712887,\n" +
                "        \"GBPMDL\": 22.532195,\n" +
                "        \"GBPMGA\": 5481.387035,\n" +
                "        \"GBPMKD\": 70.052163,\n" +
                "        \"GBPMMK\": 2629.537883,\n" +
                "        \"GBPMNT\": 4397.762623,\n" +
                "        \"GBPMOP\": 10.124482,\n" +
                "        \"GBPMRO\": 447.076595,\n" +
                "        \"GBPMUR\": 56.560793,\n" +
                "        \"GBPMVR\": 19.223018,\n" +
                "        \"GBPMWK\": 1275.483383,\n" +
                "        \"GBPMXN\": 22.588029,\n" +
                "        \"GBPMYR\": 5.525845,\n" +
                "        \"GBPMZN\": 79.208603,\n" +
                "        \"GBPNAD\": 23.055618,\n" +
                "        \"GBPNGN\": 576.477944,\n" +
                "        \"GBPNIO\": 45.68494,\n" +
                "        \"GBPNOK\": 12.946017,\n" +
                "        \"GBPNPR\": 163.995047,\n" +
                "        \"GBPNZD\": 1.988766,\n" +
                "        \"GBPOMR\": 0.482118,\n" +
                "        \"GBPPAB\": 1.252128,\n" +
                "        \"GBPPEN\": 4.724999,\n" +
                "        \"GBPPGK\": 4.408111,\n" +
                "        \"GBPPHP\": 69.262467,\n" +
                "        \"GBPPKR\": 360.510446,\n" +
                "        \"GBPPLN\": 5.24929,\n" +
                "        \"GBPPYG\": 8961.720258,\n" +
                "        \"GBPQAR\": 4.559052,\n" +
                "        \"GBPRON\": 5.592465,\n" +
                "        \"GBPRSD\": 132.801836,\n" +
                "        \"GBPRUB\": 101.903504,\n" +
                "        \"GBPRWF\": 1386.939965,\n" +
                "        \"GBPSAR\": 4.697785,\n" +
                "        \"GBPSBD\": 10.394275,\n" +
                "        \"GBPSCR\": 16.292186,\n" +
                "        \"GBPSDG\": 750.759785,\n" +
                "        \"GBPSEK\": 12.866226,\n" +
                "        \"GBPSGD\": 1.655743,\n" +
                "        \"GBPSHP\": 1.523756,\n" +
                "        \"GBPSLE\": 27.304283,\n" +
                "        \"GBPSLL\": 24733.240881,\n" +
                "        \"GBPSOS\": 713.182097,\n" +
                "        \"GBPSRD\": 46.084602,\n" +
                "        \"GBPSTD\": 25920.412692,\n" +
                "        \"GBPSVC\": 10.95574,\n" +
                "        \"GBPSYP\": 3145.853505,\n" +
                "        \"GBPSZL\": 23.055583,\n" +
                "        \"GBPTHB\": 42.591891,\n" +
                "        \"GBPTJS\": 13.67985,\n" +
                "        \"GBPTMT\": 4.395629,\n" +
                "        \"GBPTND\": 3.803909,\n" +
                "        \"GBPTOP\": 2.94783,\n" +
                "        \"GBPTRY\": 24.20914,\n" +
                "        \"GBPTTD\": 8.502436,\n" +
                "        \"GBPTWD\": 38.174724,\n" +
                "        \"GBPTZS\": 2937.933357,\n" +
                "        \"GBPUAH\": 46.013048,\n" +
                "        \"GBPUGX\": 4670.144211,\n" +
                "        \"GBPUSD\": 1.252316,\n" +
                "        \"GBPUYU\": 48.475795,\n" +
                "        \"GBPUZS\": 14320.23316,\n" +
                "        \"GBPVEF\": 3060904.345142,\n" +
                "        \"GBPVES\": 30.610126,\n" +
                "        \"GBPVND\": 29360.548516,\n" +
                "        \"GBPVUV\": 149.573671,\n" +
                "        \"GBPWST\": 3.412936,\n" +
                "        \"GBPXAF\": 745.774687,\n" +
                "        \"GBPXAG\": 0.048497,\n" +
                "        \"GBPXAU\": 0.000613,\n" +
                "        \"GBPXCD\": 3.384447,\n" +
                "        \"GBPXDR\": 0.929014,\n" +
                "        \"GBPXOF\": 750.766042,\n" +
                "        \"GBPXPF\": 136.625133,\n" +
                "        \"GBPYER\": 313.42335,\n" +
                "        \"GBPZAR\": 22.640496,\n" +
                "        \"GBPZMK\": 11272.349539,\n" +
                "        \"GBPZMW\": 23.239311,\n" +
                "        \"GBPZWL\": 403.24524\n" +
                "    ]}"

        // Stub the HTTP client's `get()` method to return a mock response.
        runBlocking {
            // Call the method under test.
            val httpResponse: HttpResponse = httpClient.get(url) {
                headers {
                    append("apikey", "Ja28GjJrGIIcwXs9jGAEkwdMJJdeyjaX")
                }
            }
            val responseBody: ByteArray = httpResponse.receive()
            val json = JSONObject(responseBody.toString(Charsets.UTF_8))
             apiResponse = Gson().fromJson(json.toString(), ApiResponse::class.java)
        }

        // Verify that the HTTP client's `get()` method was called exactly once.
       assertEquals(Gson().toJson(expectedJson), Gson().toJson(apiResponse.quotes))
    }
}
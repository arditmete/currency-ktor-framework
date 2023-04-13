package dependencies

import config.Config
import controller.CurrencyController
import redis.clients.jedis.Jedis
import service.ConverterService
import validators.Validator

class Dependencies(
    private val config: Config = Config(),
    private val converterValidator: Validator = Validator(),
    private val jedis: Jedis = Jedis(),
    val converterService: ConverterService = ConverterService(config = config, jedis = jedis),
    val currencyController: CurrencyController = CurrencyController(converterService, converterValidator)
)
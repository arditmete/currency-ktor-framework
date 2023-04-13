package com

import config.Config
import org.junit.Assert.assertEquals
import org.junit.Test

class ConfigTest {
    @Test
    fun `test default values`() {
        val config = Config()
        assertEquals("Ja28GjJrGIIcwXs9jGAEkwdMJJdeyjaX", config.apiKey)
        assertEquals("https://api.apilayer.com/currency_data/live?", config.api)
    }
}
package validators

open class Validator {
    open fun validateConvertFromTo(fromCurrency: String?, toCurrency: String?, correctCurrencies: String) {
        if (fromCurrency == null || !correctCurrencies.contains(fromCurrency) || fromCurrency.length != 3) {
            throw ValidatorException("Wrong `from` parameter: `$fromCurrency`")
        }

        if (toCurrency == null || !correctCurrencies.contains(toCurrency) || toCurrency.length != 3 ) {
            throw ValidatorException("Wrong `to` parameter: `$toCurrency`")
        }
    }
}
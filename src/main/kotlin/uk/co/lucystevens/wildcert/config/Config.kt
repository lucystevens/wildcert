package uk.co.lucystevens.wildcert.config

import java.io.FileInputStream
import java.util.*

class Config(private val configFile: String) {

    private val appPrefix = "wildcert."

    private val properties by lazy {
        FileInputStream(configFile).use {
            Properties().apply {
                load(it)
            }
        }
    }

    private fun getConfig(key: String, defaultValue: String? = null): String =
        properties.getProperty("$appPrefix$key")
            ?: defaultValue
            ?: throw IllegalStateException("Missing value for non-optional property $appPrefix$key")

    // Domain provider config
    fun getDomainProviderUrl(): String =
        getConfig("domainProvider.url")

    fun getDomainProviderKey(): String =
        getConfig("domainProvider.key")

    fun getDomainProviderSecret(): String =
        getConfig("domainProvider.secret")

    // CA config
    fun getAcmeServiceUrl(): String =
        getConfig("acme.url")

    // Polling config
    fun getPollingRetryIntervalMs(): Long =
        getConfig("polling.retryIntervalMs", "3000").toLong()

    fun getPollingWaitTimeMs(): Long =
        getConfig("polling.waitTimeMs", "500").toLong()

    fun getPollingTimeoutMs(): Long =
        getConfig("polling.timeoutMs", "60000").toLong()

    // Cert config
    fun getKeyPairSize(): Int =
        getConfig("certs.keyPairSize", "4096").toInt()
}
package uk.co.lucystevens.wildcert.config

import kotlinx.serialization.json.Json
import org.koin.dsl.module
import uk.co.lucystevens.wildcert.cli.AppRunner
import uk.co.lucystevens.wildcert.handler.CertificateHandler
import uk.co.lucystevens.wildcert.handler.AccountsHandler
import uk.co.lucystevens.wildcert.services.AccountService
import uk.co.lucystevens.wildcert.services.ChallengeService
import uk.co.lucystevens.wildcert.services.KeyPairService
import uk.co.lucystevens.wildcert.services.LocalDataService
import java.io.File
import java.time.Clock
import kotlin.random.Random

object Modules {

    private val defaultDataDir by lazy {
        System.getProperty("wildcert.appData", ".")
    }

    private val utils = module {
        single { AppRunner(get(), get()) }
        single { Config() }
        single<Clock> { Clock.systemDefaultZone() }
        single<Random> { Random.Default }
        single { Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        } }
    }

    private val handlers = module {
        single { AccountsHandler(get(), get(), File(defaultDataDir)) }
        single { CertificateHandler() }
    }

    private val services = module {
        single { LocalDataService(File(defaultDataDir,"wildcert-data.json"), get()) }
        single { ChallengeService(get()) }
        single { AccountService(get()) }
        single { KeyPairService(4096)}
    }

    internal val allModules = listOf(
        utils,
        handlers,
        services
    )

}
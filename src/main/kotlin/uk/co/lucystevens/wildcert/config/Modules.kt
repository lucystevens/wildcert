package uk.co.lucystevens.wildcert.config

import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.shredzone.acme4j.Session
import uk.co.lucystevens.wildcert.cli.AppRunner
import uk.co.lucystevens.wildcert.handler.CertificateHandler
import uk.co.lucystevens.wildcert.handler.AccountsHandler
import uk.co.lucystevens.wildcert.services.data.AccountService
import uk.co.lucystevens.wildcert.services.acme.ChallengeService
import uk.co.lucystevens.wildcert.services.acme.SessionProvider
import uk.co.lucystevens.wildcert.services.data.CertificateService
import uk.co.lucystevens.wildcert.services.util.KeyPairService
import uk.co.lucystevens.wildcert.services.data.LocalDataService
import uk.co.lucystevens.wildcert.services.domains.DomainProviderService
import uk.co.lucystevens.wildcert.services.domains.GodaddyService
import uk.co.lucystevens.wildcert.services.util.CertificateReader
import uk.co.lucystevens.wildcert.services.util.PollingHandler
import java.io.File
import java.time.Clock
import kotlin.random.Random

object Modules {

    private val appData by lazy {
        System.getProperty("wildcert.appData", ".")
    }

    private val configFile by lazy {
        System.getProperty("wildcert.configFile", "wildcert.properties")
    }

    private val utils = module {
        single { AppRunner(get(), get()) }
        single { Config(configFile) }
        single<Clock> { Clock.systemDefaultZone() }
        single<Random> { Random.Default }
        single { Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        } }

        single { PollingHandler(get(), get()) }
        single { CertificateReader() }
        single { KeyPairService(get()) }
    }

    private val handlers = module {
        single { AccountsHandler(get(), get(), File(appData)) }
        single { CertificateHandler(get(), get(), get()) }
    }

    private val services = module {
        single { ChallengeService(get(), get(), get(), get()) }
        single { SessionProvider(get()) }

        single { LocalDataService(File(appData,"wildcert-data.json"), get()) }
        single { AccountService(get()) }
        single { CertificateService(get(), get())}

        single<DomainProviderService> { GodaddyService(get(), get()) }
    }

    internal val allModules = listOf(
        utils,
        handlers,
        services
    )

}
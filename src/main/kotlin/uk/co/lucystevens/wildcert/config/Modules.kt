package uk.co.lucystevens.wildcert.config

import org.koin.dsl.module
import uk.co.lucystevens.wildcert.cli.AppRunner
import uk.co.lucystevens.wildcert.handler.CertificateHandler
import uk.co.lucystevens.wildcert.handler.AccountsHandler
import java.time.Clock
import kotlin.random.Random

object Modules {

    private val utils = module {
        single { AppRunner(get(), get()) }
        single { Config() }
        single<Clock> { Clock.systemDefaultZone() }
        single<Random> { Random.Default }
    }

    private val handlers = module {
        single { AccountsHandler() }
        single { CertificateHandler() }
    }

    internal val allModules = listOf(
        utils,
        handlers
    )

}
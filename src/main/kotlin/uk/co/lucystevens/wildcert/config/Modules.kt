package uk.co.lucystevens.wildcert.config

import org.koin.dsl.module
import uk.co.lucystevens.wildcert.cli.AppRunner
import uk.co.lucystevens.wildcert.handler.RenewCertificateHandler
import uk.co.lucystevens.wildcert.handler.RequestCertificateHandler
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
        single { RequestCertificateHandler() }
        single { RenewCertificateHandler() }
    }

    internal val allModules = listOf(
        utils,
        handlers
    )

}
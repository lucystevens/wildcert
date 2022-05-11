package uk.co.lucystevens.wildcert.config

import org.koin.dsl.module
import uk.co.lucystevens.wildcert.cli.AppRunner
import java.time.Clock
import kotlin.random.Random

object Modules {

    private val utils = module {
        single { AppRunner() }
        single { Config() }
        single<Clock> { Clock.systemDefaultZone() }
        single<Random> { Random.Default }
    }

    internal val allModules = listOf(
        utils
    )

}
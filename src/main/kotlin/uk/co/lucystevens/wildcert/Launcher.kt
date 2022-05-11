package uk.co.lucystevens.wildcert

import uk.co.lucystevens.wildcert.config.Modules
import org.koin.core.context.startKoin

fun main(args: Array<String>) {
    startKoin { modules(Modules.allModules) }
    App(args).run()
}

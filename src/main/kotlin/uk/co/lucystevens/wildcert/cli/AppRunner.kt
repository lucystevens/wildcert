package uk.co.lucystevens.wildcert.cli

import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli
import uk.co.lucystevens.wildcert.logger

@OptIn(ExperimentalCli::class)
class AppRunner {

    private val logger = logger<AppRunner>()

    fun run(args: Array<String>){
        val parser = ArgParser("wildcert")
        parser.subcommands(
            RequestCommand(),
            RenewCommand()
        )
        parser.parse(args)
    }


    enum class CertificateAuthority {
        LETSENCRYPT
    }

    enum class DomainProvider {
        GODADDY
    }

}
package uk.co.lucystevens.wildcert.cli

import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli
import uk.co.lucystevens.wildcert.handler.RenewCertificateHandler
import uk.co.lucystevens.wildcert.handler.RequestCertificateHandler
import uk.co.lucystevens.wildcert.logger

@OptIn(ExperimentalCli::class)
class AppRunner(
    private val requestCertificateHandler: RequestCertificateHandler,
    private val renewCertificateHandler: RenewCertificateHandler
) {

    private val logger = logger<AppRunner>()

    fun run(args: Array<String>){
        val parser = ArgParser("wildcert")
        parser.subcommands(
            RequestCommand(requestCertificateHandler),
            RenewCommand(renewCertificateHandler)
        )
        parser.parse(args)
    }
}
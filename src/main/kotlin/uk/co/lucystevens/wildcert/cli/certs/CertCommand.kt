package uk.co.lucystevens.wildcert.cli.certs

import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import uk.co.lucystevens.wildcert.cli.ListArgType
import uk.co.lucystevens.wildcert.handler.CertificateHandler

@OptIn(ExperimentalCli::class)
class CertCommand(
    private val handler: CertificateHandler
): Subcommand("certs", "Manage certificate(s)"){

    private val command by argument(
        ArgType.Choice(listOf("renew", "request", "list"), { it }),
        description = "Request a new certificate or renew an existing one"
    )

    private val domains by option(
        ListArgType(),
        shortName = "d",
        description = "Certificate domains. Defaults to all for renewal, required when requesting new certificates."
    )

    private val output by option(
        ArgType.String,
        shortName = "o",
        description = "Directory to output new certificates to. Required when requesting new certificates."
    )

    private val expiresIn by option(
        ArgType.Int,
        shortName = "e",
        description = "Renew all certs expiring in this number of days. Ignored when requesting new certificates."
    ).default(7)

    private val account by option(
        ArgType.String,
        shortName = "a",
        description = "Account to use for requesting and renewing certificates."
    )

    override fun execute() {
        //handler.renewCertificates(domains, source, expiresIn, ca, provider)
    }
}
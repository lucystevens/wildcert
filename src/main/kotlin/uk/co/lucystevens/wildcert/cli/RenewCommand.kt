package uk.co.lucystevens.wildcert.cli

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import kotlinx.cli.required

@OptIn(ExperimentalCli::class)
class RenewCommand: Subcommand("renew", "Renew certificate(s)"){

    val domains by option(
        ListArgType(),
        shortName = "d",
        description = "Domains to renew certificates for. Defaults to all."
    )

    val source by option(
        ArgType.String,
        shortName = "s",
        description = "Directory read certificates to renew from."
    ).required()

    val expiresIn by option(
        ArgType.Int,
        shortName = "e",
        description = "Renew all certs expiring in this number of days"
    ).default(7)

    val ca by option(
        ArgType.Choice<AppRunner.CertificateAuthority>(),
        description = "Certificate authority to use."
    ).default(AppRunner.CertificateAuthority.LETSENCRYPT)

    val provider by option(
        ArgType.Choice<AppRunner.DomainProvider>(),
        description = "Domain provider to use."
    ).default(AppRunner.DomainProvider.GODADDY)

    override fun execute() {
        println("Renew domains: $domains located at $source that expire in the next $expiresIn days. " +
                "Use $ca for the certificate authority and $provider as the domain provider")
    }
}
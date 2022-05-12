package uk.co.lucystevens.wildcert.cli

import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import kotlinx.cli.required

@OptIn(ExperimentalCli::class)
class RequestCommand: Subcommand("request", "Request new certificate(s)"){

    val domains by option(
        ListArgType(),
        shortName = "d",
        description = "Domains to request certificates for."
    ).required()

    val output by option(
        ArgType.String,
        shortName = "s",
        description = "Directory to output new certificates to."
    ).required()

    val ca by option(
        ArgType.Choice<AppRunner.CertificateAuthority>(),
        description = "Certificate authority to use."
    ).default(AppRunner.CertificateAuthority.LETSENCRYPT)

    val provider by option(
        ArgType.Choice<AppRunner.DomainProvider>(),
        description = "Domain provider to use."
    ).default(AppRunner.DomainProvider.GODADDY)

    override fun execute() {
        println("Renew new certificates for $domains and write to $output." +
                "Use $ca for the certificate authority and $provider as the domain provider")
    }
}
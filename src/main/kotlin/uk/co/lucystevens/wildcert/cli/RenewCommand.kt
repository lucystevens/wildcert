package uk.co.lucystevens.wildcert.cli

import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import kotlinx.cli.required
import uk.co.lucystevens.wildcert.handler.RenewCertificateHandler

@OptIn(ExperimentalCli::class)
class RenewCommand(
    private val handler: RenewCertificateHandler
): Subcommand("renew", "Renew certificate(s)"){

    private val domains by option(
        ListArgType(),
        shortName = "d",
        description = "Domains to renew certificates for. Defaults to all."
    )

    private val source by option(
        ArgType.String,
        shortName = "s",
        description = "Directory read certificates to renew from."
    ).required()

    private val expiresIn by option(
        ArgType.Int,
        shortName = "e",
        description = "Renew all certs expiring in this number of days"
    ).default(7)

    private val ca by option(
        ArgType.Choice<CertificateAuthority>(),
        description = "Certificate authority to use."
    ).default(CertificateAuthority.LETSENCRYPT)

    private val provider by option(
        ArgType.Choice<DomainProvider>(),
        description = "Domain provider to use."
    ).default(DomainProvider.GODADDY)

    override fun execute() {
        handler.renewCertificates(domains, source, expiresIn, ca, provider)
    }
}
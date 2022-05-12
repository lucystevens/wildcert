package uk.co.lucystevens.wildcert.cli

import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import kotlinx.cli.required
import uk.co.lucystevens.wildcert.handler.RequestCertificateHandler

@OptIn(ExperimentalCli::class)
class RequestCommand(
    private val handler: RequestCertificateHandler
): Subcommand("request", "Request new certificate(s)"){

    private val domains by option(
        ListArgType(),
        shortName = "d",
        description = "Domains to request certificates for."
    ).required()

    private val output by option(
        ArgType.String,
        shortName = "o",
        description = "Directory to output new certificates to."
    ).required()

    private val ca by option(
        ArgType.Choice<CertificateAuthority>(),
        description = "Certificate authority to use."
    ).default(CertificateAuthority.LETSENCRYPT)

    private val provider by option(
        ArgType.Choice<DomainProvider>(),
        description = "Domain provider to use."
    ).default(DomainProvider.GODADDY)

    override fun execute() {
        handler.requestCertificates(domains, output, ca, provider)
    }
}
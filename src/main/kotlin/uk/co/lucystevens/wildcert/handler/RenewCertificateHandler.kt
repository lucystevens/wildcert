package uk.co.lucystevens.wildcert.handler

import uk.co.lucystevens.wildcert.cli.CertificateAuthority
import uk.co.lucystevens.wildcert.cli.DomainProvider

class RenewCertificateHandler {

    fun renewCertificates(
        domains: List<String>?,
        source: String,
        expiresIn: Int,
        certificateAuthority: CertificateAuthority,
        domainProvider: DomainProvider
    ){

        println("Renew domains: $domains located at $source that expire in the next $expiresIn days. " +
                "Use $certificateAuthority for the certificate authority and $domainProvider as the domain provider")
    }
}
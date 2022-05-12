package uk.co.lucystevens.wildcert.handler

import uk.co.lucystevens.wildcert.cli.CertificateAuthority
import uk.co.lucystevens.wildcert.cli.DomainProvider

class RequestCertificateHandler {

    fun requestCertificates(
        domains: List<String>,
        output: String,
        certificateAuthority: CertificateAuthority,
        domainProvider: DomainProvider
    ){

        println("Renew new certificates for $domains and write to $output." +
                "Use $certificateAuthority for the certificate authority and $domainProvider as the domain provider")
    }
}
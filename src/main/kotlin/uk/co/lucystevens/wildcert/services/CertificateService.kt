package uk.co.lucystevens.wildcert.services

import uk.co.lucystevens.wildcert.data.models.FullCertificateInfo

class CertificateService(
    private val localDataService: LocalDataService,
    private val certificateReader: CertificateReader
) {

    fun listCertificates(): List<FullCertificateInfo> =
        localDataService.data.certificates.map {
            val cert = certificateReader.readCert(it.certFile)
            FullCertificateInfo(
                it.certFile,
                it.defaultAccount,
                cert.subjectX500Principal.name,
                cert.notAfter)
        }
}
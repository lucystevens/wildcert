package uk.co.lucystevens.wildcert.services.data

import uk.co.lucystevens.wildcert.data.models.CertificateData
import uk.co.lucystevens.wildcert.data.models.FullCertificateInfo
import uk.co.lucystevens.wildcert.services.util.CertificateReader

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
                it.domain,
                cert.notAfter)
        }

    fun addCertificates(certs: List<CertificateData>) {
        localDataService.data.certificates.addAll(certs)
        localDataService.persistData()
    }
}
package uk.co.lucystevens.wildcert.services

import java.io.FileInputStream
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

class CertificateReader {

    fun readCert(filePath: String): X509Certificate {
        val factory = CertificateFactory.getInstance("X509")
        return FileInputStream(filePath).use {
            factory.generateCertificate(it) as X509Certificate
        }
    }
}
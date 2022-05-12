package uk.co.lucystevens.wildcert

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Test
import uk.co.lucystevens.wildcert.cli.AppRunner
import uk.co.lucystevens.wildcert.cli.CertificateAuthority
import uk.co.lucystevens.wildcert.cli.DomainProvider
import uk.co.lucystevens.wildcert.handler.RenewCertificateHandler
import uk.co.lucystevens.wildcert.handler.RequestCertificateHandler
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AppRunnerTest {

    private val requestCertificateHandler = mockk<RequestCertificateHandler>()
    private val renewCertificateHandler = mockk<RenewCertificateHandler>()
    private val appRunner = AppRunner(requestCertificateHandler, renewCertificateHandler)

    @Test
    fun renewCertificates_whenAllArgsSpecified(){
        // arrange
        val args = arrayOf("renew",
            "-d", "example.com,example.co.uk",
            "-s", "/home/user/certs",
            "-e", "10",
            "--ca", "letsencrypt",
            "--provider", "godaddy"
        )

        val domains = slot<List<String>>()
        val source = slot<String>()
        val expiresIn = slot<Int>()
        val ca = slot<CertificateAuthority>()
        val provider = slot<DomainProvider>()
        every { renewCertificateHandler.renewCertificates(
            capture(domains),
            capture(source),
            capture(expiresIn),
            capture(ca),
            capture(provider)
        ) } answers { }

        // action
        appRunner.run(args)

        // assert
        assertEquals(listOf("example.com", "example.co.uk"), domains.captured)
        assertEquals("/home/user/certs", source.captured)
        assertEquals(10, expiresIn.captured)
        assertEquals(CertificateAuthority.LETSENCRYPT, ca.captured)
        assertEquals(DomainProvider.GODADDY, provider.captured)
    }

    @Test
    fun renewCertificates_whenOnlyRequiredArgsSpecified(){
        // arrange
        val args = arrayOf("renew", "-s", "/home/user/certs")

        val domains = mutableListOf<List<String>?>()
        val source = slot<String>()
        val expiresIn = slot<Int>()
        val ca = slot<CertificateAuthority>()
        val provider = slot<DomainProvider>()
        every { renewCertificateHandler.renewCertificates(
            captureNullable(domains),
            capture(source),
            capture(expiresIn),
            capture(ca),
            capture(provider)
        ) } answers { }

        // action
        appRunner.run(args)

        // assert
        assertNull(domains.first())
        assertEquals("/home/user/certs", source.captured)
        assertEquals(7, expiresIn.captured)
        assertEquals(CertificateAuthority.LETSENCRYPT, ca.captured)
        assertEquals(DomainProvider.GODADDY, provider.captured)
    }

    @Test
    fun requestCertificates_whenAllArgsSpecified(){
        // arrange
        val args = arrayOf("request",
            "-d", "example.com,example.co.uk",
            "-o", "/home/user/certs",
            "--ca", "letsencrypt",
            "--provider", "godaddy"
        )

        val domains = slot<List<String>>()
        val output = slot<String>()
        val ca = slot<CertificateAuthority>()
        val provider = slot<DomainProvider>()
        every { requestCertificateHandler.requestCertificates(
            capture(domains),
            capture(output),
            capture(ca),
            capture(provider)
        ) } answers { }

        // action
        appRunner.run(args)

        // assert
        assertEquals(listOf("example.com", "example.co.uk"), domains.captured)
        assertEquals("/home/user/certs", output.captured)
        assertEquals(CertificateAuthority.LETSENCRYPT, ca.captured)
        assertEquals(DomainProvider.GODADDY, provider.captured)
    }

    @Test
    fun requestCertificates_whenOnlyRequiredArgsSpecified(){
        // arrange
        val args = arrayOf("request",
            "-d", "example.com",
            "-o", "/home/user/certs",)

        val domains = slot<List<String>>()
        val output = slot<String>()
        val ca = slot<CertificateAuthority>()
        val provider = slot<DomainProvider>()
        every { requestCertificateHandler.requestCertificates(
            capture(domains),
            capture(output),
            capture(ca),
            capture(provider)
        ) } answers { }

        // action
        appRunner.run(args)

        // assert
        assertEquals(listOf("example.com"), domains.captured)
        assertEquals("/home/user/certs", output.captured)
        assertEquals(CertificateAuthority.LETSENCRYPT, ca.captured)
        assertEquals(DomainProvider.GODADDY, provider.captured)
    }
}
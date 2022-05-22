package uk.co.lucystevens.wildcert.services.acme

import org.shredzone.acme4j.Account
import org.shredzone.acme4j.AccountBuilder
import org.shredzone.acme4j.Session
import org.shredzone.acme4j.Status
import org.shredzone.acme4j.challenge.Dns01Challenge
import org.shredzone.acme4j.util.CSRBuilder
import uk.co.lucystevens.wildcert.config.Config
import uk.co.lucystevens.wildcert.logger
import uk.co.lucystevens.wildcert.services.domains.DomainProviderService
import uk.co.lucystevens.wildcert.services.util.KeyPairService
import uk.co.lucystevens.wildcert.services.util.PollingHandler
import java.io.File
import java.io.FileWriter
import java.net.URL


class ChallengeService(
    private val keyPairService: KeyPairService,
    private val pollingHandler: PollingHandler,
    private val domainProviderService: DomainProviderService,
    sessionProvider: SessionProvider
    ) {

    private val logger = logger<ChallengeService>()

    private val txtRecordName = "_acme-challenge"
    private val session = sessionProvider.createSession()

    fun createAccount(email: String, keyPairFile: File): String {
        logger.info("Creating account for email $email")
        val accountKeyPair = keyPairService.getOrCreateKeyPair(keyPairFile)

        // Doesn't work when running with gradle, disabling for time being
        /*val tocLink = session.metadata.termsOfService
        println("Do you agree to the terms of service? (Y/n) ($tocLink)")
        val answer = readLine()?.lowercase()
        if(answer != "y" && answer != "yes")
            throw IllegalStateException("Terms of service declined.")*/

        val account = AccountBuilder()
            .addContact("mailto:$email")
            .agreeToTermsOfService()
            .useKeyPair(accountKeyPair)
            .create(session)

        val accountLocator = account.location.toString()
        logger.info("Account created successfully. Locator: $accountLocator")
        return accountLocator
    }

    fun login(accountUrl: String, keyPairFile: File): Account{
        logger.info("Logging in to account $accountUrl")
        val keyPair = keyPairService.readKeyPair(keyPairFile)
        val login = session.login(URL(accountUrl), keyPair)
        logger.info("Successfully logged in to account ${login.accountLocation}")
        return login.account
    }

    fun requestCert(account: Account, domain: String, outputDir: File): File {
        logger.info("Requesting a new wildcard certificate for $domain")
        val order = account.newOrder()
            .domains("*.$domain", domain)
            .create()

        // Process auth challenges
        for (auth in order.authorizations) {
            if (auth.status == Status.PENDING) {
                val challenge = auth.findChallenge(Dns01Challenge::class.java)?:
                    throw IllegalStateException("Could not find dns-01 challenge.")
                val challengeDomain = auth.identifier.domain
                val digest = challenge.digest

                logger.info("Creating TXT record $txtRecordName=$digest for $domain")
                // Submit TXT record
                domainProviderService.createTxtRecord(challengeDomain, txtRecordName, digest)

                // TODO need to wait for DNS propagation here
                // TODO submit all TXT records at the same time for speed
                // TODO Maybe just use cloudflare instead

                challenge.trigger()

                logger.info("Waiting for authorisation to complete")
                val timeTaken = pollingHandler.waitForComplete(
                    getStatus = { auth.status },
                    update = { auth.update() }
                )
                logger.info("Completed authorisation in ${timeTaken}ms. URl: ${auth.location} Data: ${auth.json}")

                // Remove TXT record
                logger.info("Removing TXT record $txtRecordName for $domain")
                domainProviderService.deleteTxtRecord(challengeDomain, txtRecordName)
            }
        }

        // Wait for order to be ready
        logger.info("Waiting for certificate order to be ready")
        val timeTakenToReady = pollingHandler.waitForReady(
            getStatus = { order.status },
            update = { order.update() }
        )
        logger.info("Order ready in ${timeTakenToReady}ms")

        // Create private key pair for cert
        val domainKeyPair = keyPairService.createKeyPair(File(outputDir, "$domain.pem"))

        // Create CSR
        logger.info("Creating CSR for $domain")
        val csrb = CSRBuilder().apply {
            addDomain(domain)
            addDomain("*.$domain")
            //setOrganization("The Example Organization")
            sign(domainKeyPair)
            write(FileWriter(File(outputDir, "$domain.csr")))
        }
        order.execute(csrb.encoded)

        // Download cert
        logger.info("Waiting for certificate order to complete")
        val timeTakenToComplete = pollingHandler.waitForComplete(
            getStatus = { order.status },
            update = { order.update() }
        )
        if(order.status == Status.INVALID)
            throw IllegalStateException("Order invalid. Response: ${order.error?.asJSON()}")
        logger.info("Completed order in ${timeTakenToComplete}ms")

        val cert = order.certificate?:
            throw IllegalStateException("Could not find certificate for $domain.")
        val certFile = File(outputDir, "$domain.crt")
        FileWriter(certFile).use {
            cert.writeCertificate(it)
        }
        return certFile
    }

}
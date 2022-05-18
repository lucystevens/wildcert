package uk.co.lucystevens.wildcert.services

import org.shredzone.acme4j.Account
import org.shredzone.acme4j.AccountBuilder
import org.shredzone.acme4j.Session
import org.shredzone.acme4j.Status
import org.shredzone.acme4j.challenge.Dns01Challenge
import org.shredzone.acme4j.util.CSRBuilder
import org.shredzone.acme4j.util.KeyPairUtils
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.net.URL
import java.util.Scanner


class ChallengeService(private val keyPairService: KeyPairService) {

    private val letsEncryptStageUrl = "acme://letsencrypt.org/staging"
    private val letsEncryptProdUrl = "acme://letsencrypt.org"

    private val session = Session(letsEncryptStageUrl)

    fun createAccount(email: String, keyPairFile: File): String{
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

        return account.location.toString()
    }

    fun login(accountUrl: String, keyPairFile: File): Account{
        val keyPair = keyPairService.readKeyPair(keyPairFile)
        val login = session.login(URL(accountUrl), keyPair)
        return login.account
    }

    fun requestCert(account: Account, domain: String, outputDir: String){
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

                // Submit TXT record

                challenge.trigger()

                // TODO handle this better
                while (auth.status != Status.VALID) {
                    Thread.sleep(3000L);
                    auth.update();
                }

                // Remove TXT record
            }
        }

        // Create private key pair for cert
        val domainKeyPair = keyPairService.createKeyPair(File(outputDir, "$domain.pem"))

        // Create CSR
        val csrb = CSRBuilder().apply {
            addDomain(domain)
            addDomain("*.$domain")
            //setOrganization("The Example Organization")
            sign(domainKeyPair)
            write(FileWriter(File(outputDir, "$domain.csr")))
        }
        order.execute(csrb.encoded)

        // Download cert
        while (order.status != Status.VALID) {
            Thread.sleep(3000L)
            order.update()
        }
        val cert = order.certificate?:
            throw IllegalStateException("Could not find certificate for $domain.")
        FileWriter(File(outputDir, "$domain.crt")).use {
            cert.writeCertificate(it)
        }
    }
}
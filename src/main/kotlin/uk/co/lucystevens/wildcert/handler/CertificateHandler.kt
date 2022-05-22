package uk.co.lucystevens.wildcert.handler

import org.shredzone.acme4j.Account
import uk.co.lucystevens.wildcert.cli.lib.exception.HandlerException
import uk.co.lucystevens.wildcert.data.models.AccountData
import uk.co.lucystevens.wildcert.data.models.CertificateData
import uk.co.lucystevens.wildcert.logger
import uk.co.lucystevens.wildcert.mapNotException
import uk.co.lucystevens.wildcert.services.acme.ChallengeService
import uk.co.lucystevens.wildcert.services.data.AccountService
import uk.co.lucystevens.wildcert.services.data.CertificateService
import java.io.File

class CertificateHandler(
    private val accountService: AccountService,
    private val certificateService: CertificateService,
    private val challengeService: ChallengeService
    ) {

    private val logger = logger<CertificateHandler>()

    fun request(
        domains: List<String>,
        output: String,
        accountName: String?
    ){
        val accountData = accountName?.let{
            accountService.getAccountByName(it)
                ?: throw HandlerException("No account found with name '$it'.")
        } ?: accountService.getDefaultAccount()
            ?: throw HandlerException("No default environment found. Please set one up using 'accounts add'.")

        val outputDir = File(output)
        if(!outputDir.exists())
            throw HandlerException("Specified output directory '$output' does not exist.")

        if(!outputDir.isDirectory)
            throw HandlerException("Specified output directory '$output' is not a directory.")

        if(domains.isEmpty())
            throw HandlerException("At least one domain must be specified.")

        val account = challengeService.login(accountData.caUrl, File(accountData.keyPairFile))


        val certData = domains.mapNotException(
            tryThis = {
                val certFile = requestOne(it, outputDir, account)
                CertificateData(certFile.absolutePath, it, accountData.name)
            },
            handleError = {
                logger.error("Error when requesting certificate for $it", it)
            })
        certificateService.addCertificates(certData)
    }

    fun requestOne(domain: String, output: File, account: Account): File{
        if(!domain.matches(Regex("""[\w-.]+""")))
            throw IllegalArgumentException("$domain is not a valid domain.")

        return challengeService.requestCert(account, domain, output)
    }

    fun renew(
        domains: List<String>?,
        expiresIn: Int,
        account: String?
    ){

    }

    fun list(){

    }
}
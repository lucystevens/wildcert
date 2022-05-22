package uk.co.lucystevens.wildcert.handler

import uk.co.lucystevens.wildcert.cli.lib.exception.HandlerException
import uk.co.lucystevens.wildcert.data.models.AccountData
import uk.co.lucystevens.wildcert.services.data.AccountService
import uk.co.lucystevens.wildcert.services.acme.ChallengeService
import java.io.File

class AccountsHandler(
    private val accountService: AccountService,
    private val challengeService: ChallengeService,
    private val defaultDataDir: File
) {

    fun add(
        name: String,
        email: String,
        keyPair: String?,
        isDefault: Boolean?
    ){
        // Check name is unique
        accountService.getAccountByName(name)?.let {
            throw HandlerException("Account with name '$name' already exists.")
        }

        // Check default
        val defaultAccount = accountService.getDefaultAccount()
        val default = if(defaultAccount != null){
            if(isDefault == true) {
                defaultAccount.isDefault = false
                true
            } else false
        }
        else {
            if(isDefault == false)
                throw HandlerException("There must be a default account.")
            else true
        }

        // Check keyPair
        val keyPairFile = keyPair?.let { File(it) }?: File(defaultDataDir, "$name.pem")

        val caUrl = challengeService.createAccount(email, keyPairFile)
        val account = AccountData(name, email, caUrl, keyPairFile.absolutePath, default)
        accountService.addAccount(account)
    }


    fun list(){
        println(generateAccountsList())
    }

    fun generateAccountsList(): String {
        val accounts = accountService.listAccounts()
        return StringBuilder().apply {
            if(accounts.isEmpty()) appendLine("No accounts found.")
            else appendLine("Accounts:")
            accounts.forEach { appendLine("\t$it") }
        }.toString()
    }
}
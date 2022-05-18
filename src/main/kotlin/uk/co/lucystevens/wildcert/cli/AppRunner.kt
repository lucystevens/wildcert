package uk.co.lucystevens.wildcert.cli

import uk.co.lucystevens.wildcert.cli.lib.CliBuilder.Companion.command
import uk.co.lucystevens.wildcert.cli.lib.CliBuilder.Companion.execute
import uk.co.lucystevens.wildcert.cli.lib.CliBuilder.Companion.option
import uk.co.lucystevens.wildcert.cli.lib.CliParser
import uk.co.lucystevens.wildcert.handler.AccountsHandler
import uk.co.lucystevens.wildcert.handler.CertificateHandler

class AppRunner(
    private val accountsHandler: AccountsHandler,
    private val certsHandler: CertificateHandler
) {

    fun run(args: Array<String>){
        val parser = CliParser("wildcert")
        parser.parse(args) {
            command("accounts", "Manage account(s)"){
                command("add", "Add a new account"){
                    val name by option(Options.name).required()
                    val email by option(Options.email).required()
                    val keyPair by option(Options.keyPair).optional()
                    val default by option(Options.default).optional()
                    execute { accountsHandler.add(name, email, keyPair, default) }
                }
                command("list", "List all accounts"){
                    execute { accountsHandler.list() }
                }
            }
            command("certs", "Manage certificate(s)"){
                command("request", "Request new certificate(s)"){
                    val domains by option(Options.domains).required()
                    val output by option(Options.output).required()
                    val account by option(Options.account).optional()
                    execute { certsHandler.request(domains, output, account) }
                }
                command("renew", "Renew expiring certificate(s)"){
                    val domains by option(Options.domains).optional()
                    val expiresIn by option(Options.expiresIn).default(7)
                    val account by option(Options.account).optional()
                    execute { certsHandler.renew(domains, expiresIn, account) }
                }
                command("list", "List all certificates"){
                    execute { certsHandler.list() }
                }
            }
        }

    }


}
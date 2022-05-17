package uk.co.lucystevens.wildcert.cli

import kotlinx.cli.ExperimentalCli
import uk.co.lucystevens.wildcert.cli.lib.CliBuilder.Companion.command
import uk.co.lucystevens.wildcert.cli.lib.CliBuilder.Companion.execute
import uk.co.lucystevens.wildcert.cli.lib.CliBuilder.Companion.option
import uk.co.lucystevens.wildcert.cli.lib.option.FullOption
import uk.co.lucystevens.wildcert.cli.lib.CliParser
import uk.co.lucystevens.wildcert.cli.lib.OptionType
import uk.co.lucystevens.wildcert.handler.CertificateHandler
import uk.co.lucystevens.wildcert.handler.AccountsHandler
import uk.co.lucystevens.wildcert.logger

@OptIn(ExperimentalCli::class)
class AppRunner(
    private val accountsHandler: AccountsHandler,
    private val certsHandler: CertificateHandler
) {

    private val logger = logger<AppRunner>()

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
        }

    }


}
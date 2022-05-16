package uk.co.lucystevens.wildcert.cli

import kotlinx.cli.ExperimentalCli
import uk.co.lucystevens.wildcert.cli.lib.CliBuilder.Companion.command
import uk.co.lucystevens.wildcert.cli.lib.CliBuilder.Companion.execute
import uk.co.lucystevens.wildcert.cli.lib.CliBuilder.Companion.option
import uk.co.lucystevens.wildcert.cli.lib.Option
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

        val keyPairOption = Option(
            OptionType.string,
            shortName = "k",
            fullName = "keyPair",
            description = "Key pair file for this account. If it doesn't exist or is unspecified, a new file will be created."
        )

        val defaultOption = Option(
            OptionType.boolean,
            shortName = "d",
            fullName = "default",
            description = "Whether this account should be the default account. Defaults to true for the first account"
        )


        val parser = CliParser("wildcert")
        parser.parse(args) {
            command("accounts"){
                command("add"){
                    val name = option(nameOption(required = true))
                    val email = option(emailOption(required = true))
                    val keyPair = option(keyPairOption)
                    val default = option(defaultOption)
                    execute { accountsHandler.add(name!!, email!!, keyPair, default) }
                }
            }
        }

    }

    fun nameOption(required: Boolean) = Option(
        OptionType.string,
        shortName = "n",
        fullName = "name",
        description = "Name for this account. Must be unique.",
        required = required
    )

    fun emailOption(required: Boolean) = Option(
        OptionType.string,
        shortName = "e",
        fullName = "email",
        description = "Email address for this account.",
        required = required
    )
}
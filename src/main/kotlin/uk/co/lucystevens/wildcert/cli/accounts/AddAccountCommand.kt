package uk.co.lucystevens.wildcert.cli.accounts

import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import kotlinx.cli.required
import uk.co.lucystevens.wildcert.handler.AccountsHandler

@OptIn(ExperimentalCli::class)
class AddAccountCommand(
    private val handler: AccountsHandler
): Subcommand("add", "Add a new account"){

    private val accountName by option(
        ArgType.String,
        shortName = "n",
        fullName = "name",
        description = "Name for this account. Must be unique."
    ).required()

    private val email by option(
        ArgType.String,
        shortName = "e",
        description = "Email address for this account."
    ).required()

    private val keyPair by option(
        ArgType.String,
        shortName = "k",
        description = "Key pair file for this account. If it doesn't exist or is unspecified, a new file will be created."
    )

    private val default by option(
        ArgType.Boolean,
        shortName = "d",
        description = "Whether this account should be the default account. Defaults to true for the first account"
    )

    override fun execute() {

        handler.add(accountName, email, keyPair, default)
    }
}
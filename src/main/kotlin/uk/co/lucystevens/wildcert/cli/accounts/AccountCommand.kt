package uk.co.lucystevens.wildcert.cli.accounts

import kotlinx.cli.ArgType
import kotlinx.cli.ExperimentalCli
import kotlinx.cli.Subcommand
import kotlinx.cli.default
import kotlinx.cli.required
import uk.co.lucystevens.wildcert.cli.CertificateAuthority
import uk.co.lucystevens.wildcert.cli.DomainProvider
import uk.co.lucystevens.wildcert.cli.ListArgType
import uk.co.lucystevens.wildcert.handler.AccountsHandler

@OptIn(ExperimentalCli::class)
class AccountCommand(
    private val handler: AccountsHandler
): Subcommand("accounts", "Manage account(s)"){

    init {
        subcommands(
            AddAccountCommand(handler)
        )
    }

    override fun execute() { }
}
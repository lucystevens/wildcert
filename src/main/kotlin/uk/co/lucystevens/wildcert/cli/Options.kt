package uk.co.lucystevens.wildcert.cli

import uk.co.lucystevens.wildcert.cli.lib.OptionType
import uk.co.lucystevens.wildcert.cli.lib.option.Option

object Options {

    val name = Option(
        OptionType.string,
        shortName = "n",
        fullName = "name",
        description = "Name for this account. Must be unique."
    )

    val email = Option(
        OptionType.string,
        shortName = "e",
        fullName = "email",
        description = "Email address for this account."
    )

    val keyPair = Option(
        OptionType.string,
        shortName = "k",
        fullName = "keyPair",
        description = "Key pair file for this account. If it doesn't exist or is unspecified, a new file will be created."
    )

    val default = Option(
        OptionType.boolean,
        shortName = "d",
        fullName = "default",
        description = "Whether this account should be the default account. Defaults to true for the first account"
    )
}
package uk.co.lucystevens.wildcert.cli.lib.option

import uk.co.lucystevens.wildcert.cli.lib.OptionType

class Option<T>(
    val type: OptionType<T>,
    val shortName: String,
    val fullName: String? = null,
    val description: String? = null,
){
    internal fun toNullable(): Option<T?> =
        Option(type.nullable(), shortName, fullName, description)
}

package uk.co.lucystevens.wildcert.cli

import kotlinx.cli.ArgType

class ListArgType:
    ArgType<List<String>>(true) {

    override val description: kotlin.String
        get() = "{ Comma-separated list }"

    override fun convert(value: kotlin.String, name: kotlin.String): List<kotlin.String> =
        value.split(",")
}
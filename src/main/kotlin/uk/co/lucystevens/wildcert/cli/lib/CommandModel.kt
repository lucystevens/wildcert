package uk.co.lucystevens.wildcert.cli.lib

class CommandModel{
    val arguments = mutableListOf<String>()
    val options = mutableMapOf<String, String>()

    fun isHelp(): Boolean =
        options["-h"] == "true" || options["--help"] == "true"
}
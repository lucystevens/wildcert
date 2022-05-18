package uk.co.lucystevens.wildcert.cli.lib

import uk.co.lucystevens.wildcert.cli.lib.exception.UsageException
import uk.co.lucystevens.wildcert.cli.lib.option.FullOption

open class Command(
    val name: String,
    private val description: String?) {

    val commands = mutableMapOf<String, Command>()
    var handler: () -> Unit = { throw UsageException("No handler defined for $name.", this) }

    var error: UsageException? = null
    val options = mutableListOf<FullOption<*>>()

    fun execute(){
        if(error != null) throw error as UsageException
        else handler.invoke()
    }

    fun helpText(): String {
        val help = StringBuilder()
        if(commands.isNotEmpty()) help.appendLine("Commands: ")
        commands.values.forEach {
            if(it.description != null)
                help.appendLine("\t${it.name} -> ${it.description}")
            else
                help.appendLine("\t${it.name}")
        }

        help.appendLine("Options: ")
        options.forEach {
            help.appendLine("\t${it.helpText()}")
        }
        help.appendLine("\t-h, --help -> Usage info")

        return help.toString()
    }
}
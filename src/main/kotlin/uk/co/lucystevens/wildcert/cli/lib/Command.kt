package uk.co.lucystevens.wildcert.cli.lib

open class Command(
    val name: String,
    val parentCommand: Command?,
    val description: String?) {

    val commands = mutableMapOf<String, Command>()
    var error: UsageException? = null
    var handler = { } // TODO No handler defined logic

    val options = mutableListOf<Option<*>>()

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
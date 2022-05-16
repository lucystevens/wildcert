package uk.co.lucystevens.wildcert.cli.lib

// TODO extract into separate library at some point
class CliParser(name: String): Command(name, null, null) {

    internal lateinit var commandModel: CommandModel

    fun parse(args: Array<String>, commandGroup: () -> Unit){
        CliBuilder.setStaticParser(this)
        commandModel = parseCommandModel(args)
        commandGroup.invoke()

        println(
            if(commandModel.isHelp()) printHelp()
            else executeCommands()
        )
    }

    internal fun executeCommands(): String {
        return try {
            val currentCommand = getCurrentCommand()
            currentCommand.execute()
            ""
        } catch (e: UsageException){
            e.message?: "Unspecified error."
        }
    }

    fun printHelp(): String {
        val help = StringBuilder()
        val currentCommand = getCurrentCommand()
        help.appendLine("Usage: $name ${commandModel.arguments.joinToString(" ")} [OPTIONS]")
        help.append(currentCommand.helpText())
        return help.toString()
    }

    fun getCurrentCommand(): Command {
        var currentCommand: Command = this
        commandModel.arguments.forEach {
            currentCommand = currentCommand.commands[it] ?:
                throw UsageException(
                    "Command $it not recognised as a valid subcommand of ${currentCommand.name}.", currentCommand)
        }
        return currentCommand
    }

    internal fun parseCommandModel(args: Array<String>): CommandModel{
        var index = 0
        val commandModel = CommandModel()
        while(index < args.size){
            val arg = args[index]
            if(arg.isOption()){
                val nextArg = args.getOrNull(index+1)
                if(nextArg != null && !nextArg.isOption()){
                    commandModel.options[arg] = nextArg
                    index++
                }
                else {
                    commandModel.options[arg] = "true"
                }
            }
            else {
                commandModel.arguments.add(arg)
            }
            index++
        }
        return commandModel
    }

    private fun String.isOption(): Boolean = startsWith("-")

}
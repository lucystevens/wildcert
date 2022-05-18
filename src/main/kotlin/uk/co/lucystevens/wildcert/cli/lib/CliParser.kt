package uk.co.lucystevens.wildcert.cli.lib

import uk.co.lucystevens.wildcert.cli.lib.exception.HandlerException
import uk.co.lucystevens.wildcert.cli.lib.exception.UsageException

// TODO extract into separate library at some point
class CliParser(
    name: String,
    private val printOut: (String) -> Unit = { println(it) },
    private val printErr: (String) -> Unit = { System.err.println(it) },
    private val handleExceptions: (Exception) -> Unit = { printErr.invoke(it.message?: "Something went wrong") }
): Command(name, null) {

    internal lateinit var commandModel: CommandModel

    fun parse(args: Array<String>, commandGroup: () -> Unit){
        CliBuilder.setStaticParser(this)
        commandModel = parseCommandModel(args)
        commandGroup.invoke()


            if(commandModel.isHelp())
                printOut(printHelp())
            else executeCommands()
    }

    private fun executeCommands() {
        try {
            val currentCommand = getCurrentCommand()
            currentCommand.execute()
        } catch (e: UsageException){
            printErr(e.message?: "Unspecified error.")
        } catch (e: HandlerException){
            printErr(e.message?: "Something went wrong")
        } catch (e: Exception){
            handleExceptions(e)
        }
    }

    private fun printHelp(): String {
        val help = StringBuilder()
        val currentCommand = getCurrentCommand()
        help.appendLine("Usage: $name ${commandModel.arguments.joinToString(" ")} [OPTIONS]")
        help.append(currentCommand.helpText())
        return help.toString()
    }

    private fun getCurrentCommand(): Command {
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
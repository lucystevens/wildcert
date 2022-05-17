package uk.co.lucystevens.wildcert.cli.lib

import uk.co.lucystevens.wildcert.cli.lib.option.Option
import uk.co.lucystevens.wildcert.cli.lib.option.OptionBuilder

class CliBuilder {
    companion object {

        private val staticParser = ThreadLocal<CliParser>()
        private val currentCommand = ThreadLocal<Command>()

        internal fun setStaticParser(parser: CliParser) {
            staticParser.set(parser)
            currentCommand.set(parser)
        }

        fun command(name: String, description: String? = null, commandGroup: () -> Unit){
            val parentCommand = currentCommand.get()
            val thisCommand = Command(name, description)
            parentCommand.commands[thisCommand.name] = thisCommand
            currentCommand.set(thisCommand)
            try {
                commandGroup.invoke()
            } finally {
                currentCommand.set(parentCommand)
            }
        }

        fun <T> option(
            type: OptionType<T>,
            shortName: String,
            fullName: String? = null,
            description: String? = null
        ): OptionBuilder<T>  = option(Option(type, shortName, fullName, description))

        fun <T> option(option: Option<T>): OptionBuilder<T> {
            val command = currentCommand.get()

            val options = staticParser.get().commandModel.options
            val value = options["-${option.shortName}"]?: options["--${option.fullName}"]
            return OptionBuilder(option, command, value)
        }

        fun execute(handler: () -> Unit) {
            currentCommand.get().handler = handler
        }
    }
}
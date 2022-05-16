package uk.co.lucystevens.wildcert.cli.lib

class CliBuilder {
    companion object {

        private val staticParser = ThreadLocal<CliParser>()
        private val currentCommand = ThreadLocal<Command>()

        fun setStaticParser(parser: CliParser) {
            staticParser.set(parser)
            currentCommand.set(parser)
        }

        fun command(name: String, description: String? = null, commandGroup: () -> Unit){
            val parentCommand = currentCommand.get()
            val thisCommand = Command(name, parentCommand, description)
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
            description: String? = null,
            required: Boolean = false,
            default: T? = null
        ): T?  = option(Option(type, shortName, fullName, description, required, default))

        fun <T> option(option: Option<T>): T? {
            if(option.required && option.default != null)
                throw ConfigurationException("Option ${option.shortName} cannot have a default and be required.")

            val command = currentCommand.get()
            command.options.add(option)

            val options = staticParser.get().commandModel.options
            val value = options["-${option.shortName}"]?: options["--${option.fullName}"]
            return if(value == null){
                if(option.required) command.error = UsageException("Missing required option ${option.shortName}", command)
                option.default
            } else option.type.fromArg(value)
        }

        fun execute(handler: () -> Unit) {
            currentCommand.get().handler = handler
        }
    }
}
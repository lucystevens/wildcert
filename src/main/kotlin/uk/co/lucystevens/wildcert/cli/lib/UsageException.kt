package uk.co.lucystevens.wildcert.cli.lib

class UsageException(private val error: String, private val command: Command):
    Exception(error){

    override val message: String?
        get() = "${error.endWith(".")} Options are:\n${command.helpText()}"
    }
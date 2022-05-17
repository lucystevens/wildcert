package uk.co.lucystevens.wildcert.cli.lib.option

import uk.co.lucystevens.wildcert.cli.lib.Command
import uk.co.lucystevens.wildcert.cli.lib.UsageException
import java.util.*

class OptionBuilder<T>(
    private val option: Option<T>,
    private val command: Command,
    private val value: String?
) {

    fun required(): FullOption<T> {
        val error = UsageException("Missing required option ${option.shortName}", command)
        if(value == null) command.error = error

        return FullOption(
            option,
            required = true
        ) {
            value?.let {
                option.type.fromArg(it)
            }?: throw error
        }.also { command.options.add(it) }
    }

    fun default(default: T): FullOption<T>{
        return FullOption(
            option,
            default = Optional.ofNullable(default)
        ) {
            value?.let {
                option.type.fromArg(it)
            }?: default
        }.also { command.options.add(it) }
    }

    fun optional(): FullOption<T?>{
        return FullOption(
            option.toNullable(),
        ) {
            value?.let {
                option.type.fromArg(it)
            }
        }.also { command.options.add(it) }
    }

}
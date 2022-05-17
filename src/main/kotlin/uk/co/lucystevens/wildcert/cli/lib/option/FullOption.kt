package uk.co.lucystevens.wildcert.cli.lib.option

import uk.co.lucystevens.wildcert.cli.lib.endWith
import java.util.*
import kotlin.reflect.KProperty

class FullOption<T> internal constructor(
    private val option: Option<T>,
    private val required: Boolean = false,
    private val default: Optional<T> = Optional.empty(),
    private val getter: () -> T
){
    fun helpText(): String {
        val help = StringBuilder("-${option.shortName}")
        if(option.fullName != null) help.append(", --${option.fullName}")
        if(option.description != null) help.append(" -> ${option.description.trim().endWith(".")}")
        if(default.isPresent) help.append(" (defaults to ${default.get()})")
        if(required) help.append(" (always required)")

        help.append(" { ${option.type.description} }")
        return help.toString()
    }

    operator fun getValue(t: Any?, property: KProperty<*>): T = getter.invoke()
}

package uk.co.lucystevens.wildcert.cli.lib

class OptionType<T>(
    val description: String,
    val fromArg: (String) -> T
) {

    companion object{
        val string = OptionType("string") { it }
        val int = OptionType("int") { it.toInt() }
        val long = OptionType("long") { it.toLong() }
        val boolean = OptionType("boolean") { it.toBoolean() }
    }

    fun fromArg(arg: String): T = fromArg.invoke(arg)
}
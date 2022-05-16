package uk.co.lucystevens.wildcert.cli.lib

data class Option<T>(
    val type: OptionType<T>,
    val shortName: String,
    val fullName: String? = null,
    val description: String? = null,
    val required: Boolean = false,
    val default: T? = null
){
    fun helpText(): String {
        val help = StringBuilder("-$shortName")
        if(fullName != null) help.append(", --$fullName")
        if(description != null) help.append(" -> ${description.trim().endWith(".")}")

        if(default != null && required) help.append(" (defaults to $default, always required)")
        else if(default != null) help.append(" (defaults to $default)")
        else if(required) help.append(" (always required)")

        help.append(" { ${type.description} }")
        return help.toString()
    }
}

package uk.co.lucystevens.wildcert.cli.lib

fun String.endWith(suffix: String): String =
    if(endsWith(suffix)) this
    else "$this$suffix"
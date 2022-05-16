package uk.co.lucystevens.wildcert.cli.lib

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CliParserTest {

    @Test
    fun testCliParser(){
        val parser = CliParser("test")
        val commandModel = parser.parseCommandModel(arrayOf(
            "accounts", "add",
            "--name", "lucy",
            "-e", "lucy@mail.com",
            "--default",
            "-k", "/c/dev/keys/key.pem",
            "some-extra-arg"
        ))

        assertEquals(listOf("accounts", "add", "some-extra-arg"), commandModel.arguments)
        assertEquals(mapOf(
            "--name" to "lucy",
            "-e" to "lucy@mail.com",
            "--default" to "true",
            "-k" to "/c/dev/keys/key.pem"
        ), commandModel.options)
    }
}
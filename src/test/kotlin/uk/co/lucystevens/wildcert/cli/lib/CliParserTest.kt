package uk.co.lucystevens.wildcert.cli.lib

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import uk.co.lucystevens.wildcert.cli.lib.CliBuilder.Companion.command
import uk.co.lucystevens.wildcert.cli.lib.CliBuilder.Companion.execute
import uk.co.lucystevens.wildcert.cli.lib.CliBuilder.Companion.option

@Suppress("UNUSED_VARIABLE")
class CliParserTest {

    @Test
    fun parseCommandModel_complex(){
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

    // Parse - happy path

    @Test
    fun parse_capturesShortOption(){
        val parser = CliParser("test")
        val args = arrayOf("command", "-o", "testOpt")

        val capture = mutableListOf<String?>()
        parser.parse(args){
            command("command"){
                val opt by option(
                    OptionType.string,
                    "o", "opt",
                    "An option"
                ).optional()
                execute { capture.add(opt) }
            }
        }

        assertEquals(1, capture.size)
        assertEquals("testOpt", capture[0])
    }

    @Test
    fun parse_capturesLongOption(){
        val parser = CliParser("test")
        val args = arrayOf("command", "--opt", "testOpt")

        val capture = mutableListOf<String?>()
        parser.parse(args){
            command("command"){
                val opt by option(
                    OptionType.string,
                    "o", "opt",
                    "An option"
                ).optional()
                execute { capture.add(opt) }
            }
        }

        assertEquals(1, capture.size)
        assertEquals("testOpt", capture[0])
    }

    @Test
    fun parse_executesCorrectCommand(){
        val parser = CliParser("test")
        val args = arrayOf("command2")

        var executed = false
        parser.parse(args){
            command("command1"){
                execute { fail("'command1' command block executed") }
            }
            command("command2"){
                execute { executed = true }
            }
        }

        assertTrue(executed)
    }

    @Test
    fun parse_executesCorrectly_whenCommandTreeIsComplex(){
        val parser = CliParser("test")
        val args = arrayOf("accounts", "games", "list")

        var executed = false
        parser.parse(args){
            command("accounts"){
                command("users"){
                    command("add"){ }
                    command("list"){ }
                }
                command("games"){
                    command("add"){ }
                    command("list"){
                        execute { executed = true }
                    }
                }
                command("add"){ }
                command("list"){ }
            }
        }

        assertTrue(executed)
    }

    // Parse - errors

    @Test
    fun parse_printsError_whenRequiredOptionMissing(){
        var output: String? = null
        val parser = CliParser("test", printOut = { output = it })
        val args = arrayOf("command", "--opt", "testOpt")

        parser.parse(args){
            command("command"){
                val opt by option(
                    OptionType.string,
                    "o", "opt",
                    "An option"
                ).optional()
                val req by option(
                    OptionType.string,
                    "r", "req",
                    "Required option"
                ).required()
                execute { fail("'command' command block executed") }
            }
        }

        assertNotNull(output)
        assertLinesEqual(listOf(
            "Missing required option r. Options are:",
            "Options: ",
            "\t-o, --opt -> An option. { string }",
            "\t-r, --req -> Required option. (always required) { string }",
            "\t-h, --help -> Usage info"
        ), output!!)
    }

    @Test
    fun parse_printsError_whenNoExecuteBlockConfigured(){
        var output: String? = null
        val parser = CliParser("test", printOut = { output = it })
        val args = arrayOf("command1", "--opt", "testOpt")

        parser.parse(args){
            command("command1"){
                val opt by option(
                    OptionType.string,
                    "o", "opt",
                    "An option"
                ).optional()
            }
        }

        assertNotNull(output)
        assertLinesEqual(listOf(
            "No handler defined for command1. Options are:",
            "Options: ",
            "\t-o, --opt -> An option. { string }",
            "\t-h, --help -> Usage info"
        ), output!!)
    }

    @Test
    fun parse_printsError_whenUnrecognisedCommandSpecified(){
        var output: String? = null
        val parser = CliParser("test", printOut = { output = it })
        val args = arrayOf("command2", "--opt", "testOpt")

        parser.parse(args){
            command("command1"){
                val opt by option(
                    OptionType.string,
                    "o", "opt",
                    "An option"
                ).optional()
            }
        }

        assertNotNull(output)
        assertLinesEqual(listOf(
            "Command command2 not recognised as a valid subcommand of test. Options are:",
            "Commands: ",
            "\tcommand1",
            "Options: ",
            "\t-h, --help -> Usage info"
        ), output!!)
    }

    @Test
    fun parse_printsError_whenUnrecognisedOptionSpecified(){
        // TODO implement feature
    }

    @Test
    fun parse_throwsError_whenInvalidShortOptionConfigured(){
        // TODO implement feature
    }

    @Test
    fun parse_throwsError_whenInvalidLongOptionConfigured(){
        // TODO implement feature
    }

    @Test
    fun parse_throwsError_whenInvalidCommandConfigured(){
        // TODO implement feature
    }

    // Parse - help text
    @Test
    fun parse_printsCorrectUsageText_whenHelpOptionSpecifiedForCommand(){
        var output: String? = null
        val parser = CliParser("test", printOut = { output = it })
        val args = arrayOf("command", "-h")

        parser.parse(args){
            command("command"){
                val opt by option(
                    OptionType.string,
                    "o", "opt",
                    "An option"
                ).optional()
                val req by option(
                    OptionType.boolean,
                    "r", "req",
                    "Required option"
                ).required()
                val def by option(
                    OptionType.int,
                    "d", "default",
                    "Option with default"
                ).default(11)
                command("subcommand", "a subcommand"){
                    val otherOpt by option(
                        OptionType.string,
                        "o", "opt",
                        "Another option"
                    ).required()
                }
            }
        }

        assertNotNull(output)
        assertLinesEqual(listOf(
            "Usage: test command [OPTIONS]",
            "Commands: ",
            "\tsubcommand -> a subcommand",
            "Options: ",
            "\t-o, --opt -> An option. { string }",
            "\t-r, --req -> Required option. (always required) { boolean }",
            "\t-d, --default -> Option with default. (defaults to 11) { int }",
            "\t-h, --help -> Usage info"
        ), output!!)
    }

    @Test
    fun parse_printsCorrectUsageText_whenHelpOptionSpecifiedForSubCommand(){
        var output: String? = null
        val parser = CliParser("test", printOut = { output = it })
        val args = arrayOf("command", "subcommand", "-h")

        parser.parse(args){
            command("command"){
                val opt by option(
                    OptionType.string,
                    "o", "opt",
                    "An option"
                ).optional()
                val req by option(
                    OptionType.boolean,
                    "r", "req",
                    "Required option"
                ).required()
                val def by option(
                    OptionType.int,
                    "d", "default",
                    "Option with default"
                ).default(11)
                command("subcommand", "a subcommand"){
                    val otherOpt by option(
                        OptionType.long,
                        "o", "opt",
                        "Another option"
                    ).required()
                }
            }
        }

        assertNotNull(output)
        assertLinesEqual(listOf(
            "Usage: test command subcommand [OPTIONS]",
            "Options: ",
            "\t-o, --opt -> Another option. (always required) { long }",
            "\t-h, --help -> Usage info"
        ), output!!)
    }

    private fun assertLinesEqual(expected: List<String>, actual: String){
        val actualLines = actual.split("\n").filterNot { it.trim().isEmpty() }
        assertEquals(expected, actualLines)
    }
}
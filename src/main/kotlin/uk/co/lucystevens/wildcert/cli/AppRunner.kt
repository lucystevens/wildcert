package uk.co.lucystevens.wildcert.cli

import uk.co.lucystevens.wildcert.logger

class AppRunner {

    private val logger = logger<AppRunner>()

    fun run(args: List<String>){
        logger.info("Starting app")
    }

}
package uk.co.lucystevens.wildcert.services.acme

import org.shredzone.acme4j.Session
import uk.co.lucystevens.wildcert.config.Config

class SessionProvider(private val config: Config) {
    fun createSession(): Session = Session(config.getAcmeServiceUrl())
}
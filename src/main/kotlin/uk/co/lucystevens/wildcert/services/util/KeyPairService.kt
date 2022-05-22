package uk.co.lucystevens.wildcert.services.util

import org.shredzone.acme4j.util.KeyPairUtils
import uk.co.lucystevens.wildcert.config.Config
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.security.KeyPair

class KeyPairService(private val config: Config) {

    fun getOrCreateKeyPair(keyPairFile: File): KeyPair =
        if(keyPairFile.exists()) readKeyPair(keyPairFile)
        else createKeyPair(keyPairFile)

    fun createKeyPair(keyPairFile: File): KeyPair =
        KeyPairUtils.createKeyPair(config.getKeyPairSize()).apply {
            writeKeyPair(keyPairFile, this)
        }

    fun readKeyPair(keyPairFile: File): KeyPair =
        FileReader(keyPairFile).use {
            KeyPairUtils.readKeyPair(it)
        }

    fun writeKeyPair(keyPairFile: File, keyPair: KeyPair) =
        FileWriter(keyPairFile).use {
            KeyPairUtils.writeKeyPair(keyPair, it)
        }


}
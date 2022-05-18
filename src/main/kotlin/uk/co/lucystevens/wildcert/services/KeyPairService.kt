package uk.co.lucystevens.wildcert.services

import org.shredzone.acme4j.util.KeyPairUtils
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.security.KeyPair

class KeyPairService(private val keySize: Int) {

    fun getOrCreateKeyPair(keyPairFile: File): KeyPair =
        if(keyPairFile.exists()) readKeyPair(keyPairFile)
        else createKeyPair(keyPairFile)

    fun createKeyPair(keyPairFile: File): KeyPair =
        KeyPairUtils.createKeyPair(keySize).apply {
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
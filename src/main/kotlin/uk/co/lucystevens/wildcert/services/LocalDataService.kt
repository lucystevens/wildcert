package uk.co.lucystevens.wildcert.services

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import uk.co.lucystevens.wildcert.data.models.AppData
import java.io.File

class LocalDataService(
    private val localDataFile: String,
    private val json: Json
    ) {

    val data by lazy {
        json.decodeFromString<AppData>(File(localDataFile).readText())
    }

    fun persistData(){
        File(localDataFile).writeText(json.encodeToString(data))
    }
}
package uk.co.lucystevens.wildcert.services.data

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import uk.co.lucystevens.wildcert.data.models.AppData
import java.io.File

class LocalDataService(
    private val localDataFile: File,
    private val json: Json
    ) {

    val data by lazy {
        if(localDataFile.exists())
            json.decodeFromString(localDataFile.readText())
        else
            AppData(1, mutableListOf(), mutableListOf())
    }

    fun persistData(){
        localDataFile.writeText(json.encodeToString(data))
    }
}
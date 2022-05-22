package uk.co.lucystevens.wildcert.services.domains

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import uk.co.lucystevens.wildcert.config.Config
import java.io.IOException


class GodaddyService(
    private val config: Config,
    private val json: Json
): DomainProviderService{

    private val appJson = "application/json".toMediaType()

    override fun createTxtRecord(domain: String, name: String, data: String) {
        val client = OkHttpClient()
        val record = GodaddyRecordEntry(data, name, 600, "TXT")
        val body = json.encodeToString(listOf(record)).toRequestBody(appJson)
        val request = Request.Builder()
            .url("${config.getDomainProviderUrl()}/v1/domains/$domain/records")
            .patch(body)
            .header("Authorization", "sso-key ${config.getDomainProviderKey()}:${config.getDomainProviderSecret()}")
            .header("Content-Type", "application/json")
            .build()

        client.newCall(request).execute().use {
            if(it.code != 200)
                throw IOException("Request to create TXT record '$name=$data' for $domain failed. Response: ${it.body?.string()}")
        }
    }

    override fun deleteTxtRecord(domain: String, name: String) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("${config.getDomainProviderUrl()}/v1/domains/$domain/records/TXT/$name")
            .delete()
            .header("Authorization", "sso-key ${config.getDomainProviderKey()}:${config.getDomainProviderSecret()}")
            .build()

        client.newCall(request).execute().use {
            if(it.code != 204)
                throw IOException("Request to delete TXT record '$name' for '$domain failed. Response: ${it.body?.string()}")
        }
    }

}
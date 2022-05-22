package uk.co.lucystevens.wildcert.services.domains

import kotlinx.serialization.Serializable

@Serializable
data class GodaddyRecordEntry(
    val data: String,
    val name: String,
    val ttl: Int,
    val type: String
)
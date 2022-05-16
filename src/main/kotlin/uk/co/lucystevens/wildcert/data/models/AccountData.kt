package uk.co.lucystevens.wildcert.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AccountData(
    val name: String,
    val email: String,
    val caUrl: String,
    val keyPairFile: String,
    val isDefault: Boolean
)

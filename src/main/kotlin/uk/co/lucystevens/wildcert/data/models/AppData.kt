package uk.co.lucystevens.wildcert.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppData(
    @SerialName("version")
    val schemaVersion: Int,
    val accounts: List<AccountData>,
    val certificates: List<CertificateData>
)

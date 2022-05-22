package uk.co.lucystevens.wildcert.data.models

import kotlinx.serialization.Serializable

@Serializable
data class CertificateData(
    val certFile: String,
    val domain: String,
    val defaultAccount: String
)

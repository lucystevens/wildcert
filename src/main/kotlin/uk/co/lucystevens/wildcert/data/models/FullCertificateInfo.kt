package uk.co.lucystevens.wildcert.data.models

import java.util.*

data class FullCertificateInfo(
    val certFile: String,
    val defaultAccount: String,
    val domain: String,
    val expiry: Date
)

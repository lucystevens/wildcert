package uk.co.lucystevens.wildcert.services.domains

interface DomainProviderService {
    fun createTxtRecord(domain: String, name: String, data: String)
    fun deleteTxtRecord(domain: String, name: String)
}
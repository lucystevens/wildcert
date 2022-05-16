package uk.co.lucystevens.wildcert.services

import uk.co.lucystevens.wildcert.data.models.AccountData

class AccountService(
    private val localDataService: LocalDataService
) {

    fun getDefaultAccount(): AccountData? =
        localDataService.data.accounts.find { it.isDefault }

    fun getAccountByName(name: String): AccountData? =
        localDataService.data.accounts.find { it.name == name }
}
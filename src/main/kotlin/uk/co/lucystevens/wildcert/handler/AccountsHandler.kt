package uk.co.lucystevens.wildcert.handler

class AccountsHandler {

    fun add(
        name: String,
        email: String,
        keyPair: String?,
        isDefault: Boolean?
    ){
        println("name: $name, email: $email, keyPair: $keyPair, default: $isDefault")
    }
}
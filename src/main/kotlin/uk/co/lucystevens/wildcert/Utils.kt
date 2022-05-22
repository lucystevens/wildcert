package uk.co.lucystevens.wildcert

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T> logger(): Logger =
    LoggerFactory.getLogger(T::class.java)

fun <T,R> List<T>.mapNotException(tryThis: (T) -> (R), handleError: (Exception) -> Unit): List<R> =
    mapNotNull {
        try {
            tryThis(it)
        } catch (e: Exception){
            handleError(e)
            null
        }
    }
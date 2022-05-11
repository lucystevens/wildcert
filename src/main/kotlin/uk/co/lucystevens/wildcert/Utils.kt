package uk.co.lucystevens.wildcert

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T> logger(): Logger =
    LoggerFactory.getLogger(T::class.java)
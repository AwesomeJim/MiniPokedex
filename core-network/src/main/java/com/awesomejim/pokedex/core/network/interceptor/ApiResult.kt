package com.awesomejim.pokedex.core.network.interceptor

import java.io.PrintWriter
import java.io.StringWriter

sealed class ApiResult<T> {
    data class Success<T>(val data: T) : ApiResult<T>()

    data class Error<T>(val errorType: ErrorType) : ApiResult<T>()
}

enum class ErrorType {
    CLIENT,
    SERVER,
    GENERIC,
    IO_CONNECTION
}


fun printTrace(exception: Exception){
    val sw = StringWriter()
    exception.printStackTrace(PrintWriter(sw))
    val exceptionAsString = sw.toString()
    println(exceptionAsString)
}
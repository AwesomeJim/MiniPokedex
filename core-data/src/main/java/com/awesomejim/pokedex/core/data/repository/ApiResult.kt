package com.awesomejim.pokedex.core.data.repository

import androidx.annotation.StringRes
import com.awesomejim.pokedex.core.data.R
import com.awesomejim.pokedex.core.network.model.ClientException
import com.awesomejim.pokedex.core.network.model.ServerException
import java.io.IOException
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

@StringRes
fun ErrorType.toResourceId(): Int = when (this) {
    ErrorType.SERVER -> R.string.error_server
    ErrorType.GENERIC -> R.string.error_generic
    ErrorType.IO_CONNECTION -> R.string.error_connection
    ErrorType.CLIENT -> R.string.error_client
}

fun mapThrowableToErrorType(throwable: Throwable): ErrorType {
    val errorType = when (throwable) {
        is IOException -> ErrorType.IO_CONNECTION
        is ClientException -> ErrorType.CLIENT
        is ServerException -> ErrorType.SERVER
        else -> ErrorType.GENERIC
    }
    return errorType
}
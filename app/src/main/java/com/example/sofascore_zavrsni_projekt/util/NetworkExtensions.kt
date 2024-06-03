package com.example.sofascore_zavrsni_projekt.util

import retrofit2.HttpException
import retrofit2.Response
import com.example.sofascore_zavrsni_projekt.data.remote.Result

suspend fun <T> safeResponse(func: suspend () -> T): Result<T> {
    return try {
        val result = func.invoke()
        if (result is Response<*> && result.isSuccessful.not()) {
            Result.Error(HttpException(result))
        } else {
            Result.Success(result)
        }
    } catch (e: Throwable) {
        Result.Error(e)
    }
}
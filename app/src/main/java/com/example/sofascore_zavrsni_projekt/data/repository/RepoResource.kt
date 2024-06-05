package com.example.sofascore_zavrsni_projekt.data.repository

import com.example.sofascore_zavrsni_projekt.data.remote.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
inline fun <ResultType, RequestType> repoResource(
    crossinline shouldFetch: (ResultType?) -> Boolean = { true },
    crossinline fetch: suspend () -> Result<RequestType>,
    crossinline process: (RequestType) -> ResultType,
    crossinline load: () -> Flow<ResultType>,
    crossinline save: suspend (ResultType) -> Unit
) = flow {
    emit(Resource.Loading())
    val dbValue = load().firstOrNull()
    if (shouldFetch(dbValue)) {
        emit(Resource.Loading(dbValue))
        when (val apiResponse = fetch()) {
            is Result.Success -> {
                save(process(apiResponse.data!!))

                emitAll(load().filter { it != null }.map { Resource.Success(it) })
            }
            is Result.Error -> {
                emit(Resource.Failure(apiResponse.error))
            }
        }
    } else {

        emitAll(load().filter { it != null }.map { Resource.Success(it) })
    }
}
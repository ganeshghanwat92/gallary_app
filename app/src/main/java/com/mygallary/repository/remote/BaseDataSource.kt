package com.mygallary.repository.remote

import retrofit2.Response

abstract class BaseDataSource {

    protected suspend fun <T : Any> getResult(call: suspend () -> Response<T>): ResultWrapper<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return ResultWrapper.Success(body)
            }
            return error(response.message(), response.code())
        } catch (e: Exception) {
            return error(e.message ?: e.toString(),0)
        }
    }

    private fun <T> error(message: String,code : Int): ResultWrapper<T> {
       // Timber.e(message)
        return ResultWrapper.Error(message,code)
    }

}
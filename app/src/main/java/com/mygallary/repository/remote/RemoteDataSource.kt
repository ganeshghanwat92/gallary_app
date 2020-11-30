package com.mygallary.repository.remote

import javax.inject.Inject

class RemoteDataSource @Inject constructor(val apiService: ApiService) : BaseDataSource() {

    // val auth : String = "Client-ID 137cda6b5008a7c";
     val auth : String = com.mygallary.BuildConfig.IMGUR_AUTH

    /**
     * return result wrapper with success,loading and error status
     */
    suspend fun searchImages(query : String, page : Int) = getResult {
        apiService.searchImages(auth,page,query)
    }
}
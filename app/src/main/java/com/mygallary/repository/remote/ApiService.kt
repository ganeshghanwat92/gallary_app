package com.mygallary.repository.remote

import com.mygallary.repository.datamodel.SearchImageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/3/gallery/search/{page}")
     suspend fun searchImages(
        @Header("Authorization") auth : String,
        @Path("page") page : Int,
        @Query("q")name : String): Response<SearchImageResponse>

}
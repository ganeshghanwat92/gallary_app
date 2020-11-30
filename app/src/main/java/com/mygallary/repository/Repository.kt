package com.mygallary.repository

import com.mygallary.repository.remote.RemoteDataSource
import javax.inject.Inject

class Repository @Inject constructor(private val remoteDataSource: RemoteDataSource) {


   suspend fun searchMovies(query: String, page : Int)  = remoteDataSource.searchImages(query,page)

}
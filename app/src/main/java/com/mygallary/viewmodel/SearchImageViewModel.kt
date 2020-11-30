package com.mygallary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.mygallary.repository.Repository
import com.mygallary.repository.datamodel.Data
import com.mygallary.repository.datamodel.SearchImageResponse
import com.mygallary.repository.remote.ResultWrapper
import com.mygallary.utils.LocalLog
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by Ganesh on 30/11/20.
 * ganeshghanwat92@gmail.com
 */

class SearchImageViewModel @Inject constructor(val repository: Repository) : ViewModel() {

    private val TAG = SearchImageViewModel::class.java.name

    var currentPage = 0

    var query : String? = null

    var list = ArrayList<Data?>()

    private val _imageListLiveData = MutableLiveData<ResultWrapper<SearchImageResponse>> ()

    // Activity/Fragment will listen data changes on imageList livedata
    val imageList : MutableLiveData<ResultWrapper<SearchImageResponse>> get() = _imageListLiveData

    private var jobSearchMovie : CompletableJob? = null

    fun searchImages(query : String){

        jobSearchMovie = Job()

        jobSearchMovie?.let {

            CoroutineScope(Dispatchers.IO + it).launch {

                _imageListLiveData.postValue(ResultWrapper.Loading(true))

                val res = repository.searchMovies(query,0)

                _imageListLiveData.postValue(res)

            }
        }
    }

    fun loadMore(query : String, page : Int) =  liveData(context = CoroutineScope(Dispatchers.IO).coroutineContext) {
        LocalLog.d(TAG,"loadMore $page")
        emit(ResultWrapper.Loading(boolean = true))

       // _imageListLiveData.postValue(ResultWrapper.Loading(true))

        val response = repository.searchMovies(query, page)

       // _imageListLiveData.postValue(response)

        emit(response)

    }

    override fun onCleared() {
        super.onCleared()
        // cancel all jobs
        jobSearchMovie?.cancel()
    }


}
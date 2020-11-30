package com.mygallary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mygallary.repository.local.Image
import com.mygallary.repository.local.ImageLocalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Created by Ganesh on 30/11/20.
 * ganeshghanwat92@gmail.com
 */
class ImagePreviewViewModel @Inject constructor(val repository: ImageLocalRepository): ViewModel() {


    fun getImageComment(id :String) : LiveData<Image>{

      return repository.getImage(id)

    }

    fun addComment(id: String,link :String, comment: String) {

        /**
         * coroutine to perform room db insert operation on IO thread
         */
        CoroutineScope(Dispatchers.IO).launch {

            repository.insert(Image(id, comment, link))

        }

    }


}
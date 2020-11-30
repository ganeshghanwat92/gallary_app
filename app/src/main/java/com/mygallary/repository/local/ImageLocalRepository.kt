package com.mygallary.repository.local

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import javax.inject.Inject


/**
 * Created by Ganesh on 30/11/20.
 * ganeshghanwat92@gmail.com
 */
class ImageLocalRepository @Inject constructor(val imageDao: ImageDao) {


    /**
     * get record from image table @param = id
     * return observable live data
     */
    fun getImage(id :String) : LiveData<Image> {

        return imageDao.getImage(id)
    }


    /**
     *  insert new comment record in table
     */
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(image: Image) {
        imageDao.insert(image)
    }


}
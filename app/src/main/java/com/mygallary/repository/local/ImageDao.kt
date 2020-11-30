package com.mygallary.repository.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mygallary.Constants


/**
 * Created by Ganesh on 30/11/20.
 * ganeshghanwat92@gmail.com
 */

@Dao
interface ImageDao {


    @Query("SELECT * FROM " + Constants.TABLE_NAME_IMAGE)
    fun getAll(): List<Image>

    /*
  * Insert the object in database
  * @param note, object to be inserted
  */

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(image: Image)

    @Query("SELECT * FROM ${Constants.TABLE_NAME_IMAGE} WHERE id=:id")
    fun getImage(id: String): LiveData<Image>


}
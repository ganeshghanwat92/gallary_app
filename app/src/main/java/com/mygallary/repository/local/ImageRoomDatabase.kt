package com.mygallary.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase


/**
 * Created by Ganesh on 30/11/20.
 * ganeshghanwat92@gmail.com
 */
@Database(entities = [Image::class],version = 1, exportSchema = false)
public abstract class ImageRoomDatabase : RoomDatabase() {

    abstract fun imageDao(): ImageDao
}
package com.mygallary.repository.local

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mygallary.Constants


/**
 * Created by Ganesh on 30/11/20.
 * ganeshghanwat92@gmail.com
 */

@Entity(tableName = Constants.TABLE_NAME_IMAGE)
data class Image(

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "comment")
    val comment: String,


    @ColumnInfo(name = "link")
    val link: String


)

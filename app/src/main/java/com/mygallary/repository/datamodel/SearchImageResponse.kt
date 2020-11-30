package com.mygallary.repository.datamodel

import com.google.gson.annotations.SerializedName

data class SearchImageResponse (

    @SerializedName("data") val data : List<Data>,
    @SerializedName("success") val success : Boolean,
    @SerializedName("status") val status : Int

)
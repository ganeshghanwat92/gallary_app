package com.mygallary.repository.datamodel

import com.google.gson.annotations.SerializedName

data class Images (

	@SerializedName("id") val id : String,
	@SerializedName("title") val title : String,
	@SerializedName("description") val description : String,
	@SerializedName("datetime") val datetime : Int,
	@SerializedName("type") val type : String,
	@SerializedName("animated") val animated : Boolean,
	@SerializedName("width") val width : Int,
	@SerializedName("height") val height : Int,
	@SerializedName("size") val size : Int,
	@SerializedName("favorite") val favorite : Boolean,
	@SerializedName("nsfw") val nsfw : String,
	@SerializedName("section") val section : String,
	@SerializedName("account_url") val account_url : String,
	@SerializedName("account_id") val account_id : String,
	@SerializedName("in_gallery") val in_gallery : Boolean,
	@SerializedName("link") val link : String

)
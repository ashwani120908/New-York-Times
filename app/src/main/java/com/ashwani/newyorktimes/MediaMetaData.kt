package com.ashwani.newyorktimes

import com.google.gson.annotations.SerializedName

data class MediaMetaData(
    @SerializedName("url")
    var url : String?,
    @SerializedName("format")
    var format : String
)
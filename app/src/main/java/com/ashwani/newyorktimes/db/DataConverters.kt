package com.ashwani.newyorktimes.db

import androidx.room.TypeConverter
import com.ashwani.newyorktimes.data.Media
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataConverters {

    @TypeConverter
    fun fromMediaListToString(media : List<Media>) : String {
        return Gson().toJson(media)
    }

    @TypeConverter
    fun fromStringToMediaList(media: String) : List<Media> {
        return Gson().fromJson(media, object : TypeToken<List<Media>>(){}.type)
    }
}
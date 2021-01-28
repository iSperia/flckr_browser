package com.test.flickerbroswer.cache

import android.app.Application
import com.google.gson.Gson
import com.test.flickerbroswer.dto.PhotosDto
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

class PhotoCache(
    context: Application
) {

    private val gson = Gson()

    private val prefs = context.getSharedPreferences(PREF_NAME, 0)

    fun loadPhotos(): Maybe<PhotosDto> = if (prefs.contains(PREF_PHOTOS)) Maybe.fromCallable {
                val json = prefs.getString(PREF_PHOTOS, null)
                val photosDto = gson.fromJson<PhotosDto>(json, PhotosDto::class.java)
                photosDto
            } else Maybe.never<PhotosDto>()

    fun savePhotos(dto: PhotosDto) = Completable.fromAction {
        prefs.edit()
            .putString(PREF_PHOTOS, gson.toJson(dto))
            .commit()
    }

    companion object {
        const val PREF_NAME = "cache"
        const val PREF_PHOTOS = "photos"
    }
}
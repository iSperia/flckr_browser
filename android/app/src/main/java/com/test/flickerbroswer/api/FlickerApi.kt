package com.test.flickerbroswer.api

import com.test.flickerbroswer.dto.PhotosDto
import com.test.flickerbroswer.dto.PhotosResponse
import io.reactivex.rxjava3.core.Maybe
import retrofit2.http.GET

interface FlickerApi {

    @GET("rest/?method=flickr.photos.getRecent&api_key=da9d38d3dee82ec8dda8bb0763bf5d9c&format=json&nojsoncallback=1")
    fun getPhotos(): Maybe<PhotosResponse>
}
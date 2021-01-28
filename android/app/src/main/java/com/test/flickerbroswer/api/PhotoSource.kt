package com.test.flickerbroswer.api

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PhotoSource() {

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.flickr.com/services/")
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val flickerService = retrofit.create(FlickerApi::class.java)

    fun loadPhotos() = flickerService
        .getPhotos()
        .map { it.photos }
        .map { it.copy(photo = it.photo.subList(0, 20)) }
        .subscribeOn(Schedulers.io())
}
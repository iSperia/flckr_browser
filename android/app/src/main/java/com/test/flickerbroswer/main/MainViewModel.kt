package com.test.flickerbroswer.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.flickerbroswer.FlickerApplication
import java.util.concurrent.atomic.AtomicBoolean

sealed class PhotosState {
    class LoadingUrls : PhotosState()
    class Photos(val urls: List<String>): PhotosState()
}

class MainViewModel : ViewModel() {

    private val remoteLoaded = AtomicBoolean(false)

    val photos = MutableLiveData<PhotosState>()

    init {
        photos.postValue(PhotosState.LoadingUrls())
    }

    fun loadPhotos() {

        //transfer local photos into local Cache subject
        FlickerApplication.cache.loadPhotos().subscribe {
            if (!remoteLoaded.get()) {
                photos.postValue(PhotosState.Photos(it.photo.map { it.toUrl() }))
            }
        }

        reloadPhotos()
    }

    fun reloadPhotos() {
        FlickerApplication.source
            .loadPhotos()
            .doOnSuccess {
                Log.d(TAG, "${it.photo[5].toUrl()}")
                remoteLoaded.set(true)
                photos.postValue(PhotosState.Photos(it.photo.map { it.toUrl() }))
            }
            .flatMapCompletable { FlickerApplication.cache.savePhotos(it) }
            .subscribe({}, { Log.e(TAG, "Failed to reload photos") })
    }

    companion object {
        const val TAG = "MainViewModel"
    }
}
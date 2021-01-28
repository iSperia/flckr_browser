package com.test.flickerbroswer

import android.app.Application
import com.test.flickerbroswer.api.PhotoSource
import com.test.flickerbroswer.cache.PhotoCache

class FlickerApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        init(this)
    }

    companion object {
        lateinit var cache: PhotoCache

        lateinit var source: PhotoSource

        private fun init(context: Application) {
            cache = PhotoCache(context)
            source = PhotoSource()
        }
    }
}
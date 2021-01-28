package com.test.flickerbroswer.main

import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.flickerbroswer.R
import com.test.flickerbroswer.main.adapter.PaddingCalculator
import com.test.flickerbroswer.main.adapter.PhotoAdapter


class MainActivity : AppCompatActivity() {

    lateinit var photos: RecyclerView

    lateinit var loader: ProgressBar

    lateinit var adapter: PhotoAdapter

    var imageSizeMetric: Int = 0
    var imageGapSizeMetric: Int = 0
    var columnNumber = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        photos = findViewById(R.id.rv_photos)
        loader = findViewById(R.id.pb_loading)

        calculateMetrix()

        adapter = PhotoAdapter(this,
            imageSizeMetric,
            object: PaddingCalculator {
            override fun applyPaddings(view: View, position: Int) {
                val vert = imageGapSizeMetric / 2

                val left = if (position % columnNumber == 0) {
                    imageGapSizeMetric
                } else {
                    imageGapSizeMetric / 2
                }

                val right = if (position % columnNumber == columnNumber - 1) {
                    imageGapSizeMetric
                } else {
                    imageGapSizeMetric / 2
                }

                view.setPadding(left, vert, right, vert)
            }
        })
        photos.adapter = adapter
        photos.layoutManager = GridLayoutManager(this, columnNumber)

        val viewModel: MainViewModel by viewModels()

        viewModel.photos.observe(this, Observer {
            when (it) {
                is PhotosState.LoadingUrls -> {
                    //show loading screen
                    loader.visibility = View.VISIBLE
                    photos.visibility = View.GONE
                }
                is PhotosState.Photos -> {
                    loader.visibility = View.GONE
                    photos.visibility = View.VISIBLE
                    //launch adapter shenanigans

                    adapter.data = it.urls
                }
            }
        })

        viewModel.loadPhotos()
    }

    fun calculateMetrix() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            columnNumber = 2
            imageSizeMetric = width * 35 / 100
            imageGapSizeMetric = width * 10 / 100
        } else {
            columnNumber = 4
            imageSizeMetric = width * 20 / 100
            imageGapSizeMetric = width * 4 / 100
        }


    }
}
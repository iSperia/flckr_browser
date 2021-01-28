package com.test.flickerbroswer.main

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.test.flickerbroswer.R
import com.test.flickerbroswer.details.DetailsActivity
import com.test.flickerbroswer.main.adapter.ClickCallback
import com.test.flickerbroswer.main.adapter.PaddingCalculator
import com.test.flickerbroswer.main.adapter.PhotoAdapter


class MainActivity : AppCompatActivity() {

    lateinit var refresher: SwipeRefreshLayout

    lateinit var photos: RecyclerView
    lateinit var loader: ProgressBar
    lateinit var adapter: PhotoAdapter
    lateinit var errorGroup: Group
    lateinit var retryButton: Button

    var imageSizeMetric: Int = 0
    var imageGapSizeMetric: Int = 0
    var columnNumber = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        refresher = findViewById(R.id.srl_refresher)
        photos = findViewById(R.id.rv_photos)
        loader = findViewById(R.id.pb_loading)
        errorGroup = findViewById(R.id.g_error)
        retryButton = findViewById(R.id.b_retry)

        val viewModel: MainViewModel by viewModels()

        refresher.setOnRefreshListener {
            viewModel.reloadPhotos()
        }

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
        }, object : ClickCallback {
                override fun processClick(url: String) { showDetails(url) }
            })

        photos.adapter = adapter
        photos.layoutManager = GridLayoutManager(this, columnNumber).apply {
            savedInstanceState?.let { state ->
                state.getParcelable<Parcelable>(STATE_RECYCLER)?.let { recyclerState ->
                    this.onRestoreInstanceState(recyclerState)
                }
            }
        }

        retryButton.setOnClickListener {
            viewModel.reloadPhotos()
        }

        viewModel.photos.observe(this, Observer {
            when (it) {
                is PhotosState.LoadingUrls -> {
                    //show loading screen
                    loader.visibility = View.VISIBLE
                    refresher.visibility = View.GONE
                    errorGroup.visibility = View.GONE
                }
                is PhotosState.Photos -> {
                    loader.visibility = View.GONE
                    refresher.visibility = View.VISIBLE
                    errorGroup.visibility = View.GONE
                    //launch adapter shenanigans

                    adapter.data = it.urls

                    refresher.isRefreshing = false
                }
                is PhotosState.Error -> {
                    errorGroup.visibility = View.VISIBLE
                    loader.visibility = View.GONE
                    refresher.visibility = View.GONE
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

    private fun showDetails(url: String) {
        startActivity(Intent(this, DetailsActivity::class.java).apply {
            putExtra(DetailsActivity.ARG_URL, url)
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        photos.layoutManager?.let { outState.putParcelable(STATE_RECYCLER, it.onSaveInstanceState()) }
    }

    companion object {
        const val STATE_RECYCLER = "recycler"
    }
}
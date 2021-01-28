package com.test.flickerbroswer.details

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.test.flickerbroswer.R

class DetailsActivity : AppCompatActivity() {

    lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_details)
        image = findViewById(R.id.iv_image)

        val url = intent.getStringExtra(ARG_URL)

        Glide.with(this)
            .load(url)
            .fitCenter()
            .into(image)
    }

    companion object {
        const val ARG_URL = "url"
    }
}
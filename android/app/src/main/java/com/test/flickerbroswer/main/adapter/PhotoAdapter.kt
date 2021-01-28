package com.test.flickerbroswer.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.test.flickerbroswer.R

interface PaddingCalculator {
    fun applyPaddings(view: View, position: Int)
}

class PhotoAdapter(
    val context: Context,
    val imageSize: Int,
    val paddingCalculator: PaddingCalculator
) : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {

    var data: List<String> = emptyList()
        set(value) {
            field = value
            //TODO: maybe diff utils?
            notifyDataSetChanged()
        }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.iv_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.element_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = data[position]

        paddingCalculator.applyPaddings(holder.image, position)

        Glide.with(context)
            .load(photo)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .override(imageSize, imageSize)
            .centerCrop()
            .into(holder.image)
    }

    override fun getItemCount() = data.size
}
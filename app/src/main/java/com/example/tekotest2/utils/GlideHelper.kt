package com.example.tekotest2.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.tekotest2.R

class GlideHelper {

    companion object{
        fun loadUrl(url : String,image : ImageView){
            val context = image.context
            context?.let {
                Glide.with(context)
                    .load(url)
                    .apply(RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).placeholder(R.drawable.placeholder))
                    .into(image)

            }
        }
    }

}
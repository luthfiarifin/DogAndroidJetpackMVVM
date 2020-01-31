package com.laam.dogandroidjetpacknew.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.laam.dogandroidjetpacknew.R

val PERMISSION_SEND_SMS = 234

fun Context.getProgressBarDrawable(): CircularProgressDrawable =
    CircularProgressDrawable(this).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }

fun ImageView.loadImage(url: String?){
    val option = RequestOptions()
        .placeholder(context.getProgressBarDrawable())
        .error(R.drawable.dog)

    Glide.with(context)
        .setDefaultRequestOptions(option)
        .load(url)
        .into(this)
}

@BindingAdapter("android:imageUrl")
fun loadImageUrl(view: ImageView, url: String?){
    view.loadImage(url)
}

package com.kt.apps.media.core.utils

import android.widget.ImageView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

fun ImageView.loadImageUrl(
    url: String,
    placeHolder: Int? = null,
    error: Int? = null
) {
    GlideApp.with(this)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .placeholder(placeHolder ?: com.kt.skeleton.R.drawable.anim_list_base)
        .error(error ?: com.kt.skeleton.R.drawable.anim_list_base)
        .into(this)
}
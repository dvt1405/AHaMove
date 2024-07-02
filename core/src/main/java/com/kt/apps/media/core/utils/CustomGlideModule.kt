package com.kt.apps.media.core.utils

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.executor.GlideExecutor
import com.bumptech.glide.module.AppGlideModule

@GlideModule
class CustomGlideModule : AppGlideModule() {
    override fun isManifestParsingEnabled(): Boolean {
        return true
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val service = GlideExecutor.newSourceBuilder()
            .setThreadCount(GLIDE_THREAD_POOL_SIZE)
            .setName(GLIDE_EXECUTOR_NAME)
            .setUncaughtThrowableStrategy(GlideExecutor.UncaughtThrowableStrategy.DEFAULT)
            .build()
        builder.setSourceExecutor(service)
            .setAnimationExecutor(
                GlideExecutor.newSourceBuilder()
                    .setThreadCount(GLIDE_THREAD_POOL_SIZE)
                    .setName(GLIDE_ANIMATION_EXECUTOR_NAME)
                    .setUncaughtThrowableStrategy(GlideExecutor.UncaughtThrowableStrategy.DEFAULT)
                    .build()
            )
            .setDiskCacheExecutor(
                GlideExecutor.newSourceBuilder()
                    .setThreadCount(GLIDE_THREAD_POOL_SIZE)
                    .setName(GLIDE_DISK_EXECUTOR_NAME)
                    .setUncaughtThrowableStrategy(GlideExecutor.UncaughtThrowableStrategy.DEFAULT)
                    .build()
            )
    }

    companion object {
        private const val GLIDE_THREAD_POOL_SIZE = 4 // Define your thread pool size here
        private const val GLIDE_EXECUTOR_NAME = "GlideExecutor"
        private const val GLIDE_DISK_EXECUTOR_NAME = "DiskCacheExecutor"
        private const val GLIDE_ANIMATION_EXECUTOR_NAME = "AnimationExecutor"
    }
}
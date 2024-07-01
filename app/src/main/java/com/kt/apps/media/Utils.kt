package com.kt.apps.media

import com.kt.apps.media.ahamove.R

val colorMapping by lazy {
    mapOf(
        "java" to R.color.repo_color_language_java,
        "javascript" to R.color.repo_color_language_java_script,
        "html" to R.color.repo_color_language_html,
        "go" to R.color.repo_color_language_go,
        "python" to R.color.repo_color_language_python,
        "rust" to R.color.repo_color_language_rust,
        "kotlin" to R.color.repo_color_language_kotlin,
    )
}

fun String.getBackgroundColor(): Int {
    return colorMapping[this.lowercase()] ?: R.color.repo_color_language_java
}
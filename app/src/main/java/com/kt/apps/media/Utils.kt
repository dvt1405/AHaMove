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
        "dart" to R.color.repo_color_language_dart,
        "kotlin" to R.color.repo_color_language_kotlin,
        "c++" to R.color.repo_color_language_c_plus,
        "c" to R.color.repo_color_language_c,
        "shell" to R.color.repo_color_language_shell,
        "typescript" to R.color.repo_color_language_type_script,
        "objective-c++" to R.color.repo_color_language_objective_c,
        "ruby" to R.color.repo_color_language_ruby,
    )
}

fun String.getBackgroundColor(): Int {
    return colorMapping[this.lowercase()] ?: R.color.repo_color_language_java
}
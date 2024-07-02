package com.kt.apps.media.ahamove

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.kt.apps.media.ahamove.databinding.ActivityMainBinding
import com.kt.apps.media.core.models.DataState
import com.kt.apps.media.core.utils.format
import com.kt.skeleton.KunSkeleton
import com.kt.skeleton.RecyclerViewSkeletonScreen
import com.kt.skeleton.ViewSkeletonScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()
    private val adapter by lazy { GithubRepoAdapter() }
    private var userFormLoading: ViewSkeletonScreen? = null
    private var reposLoading: RecyclerViewSkeletonScreen? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.swipeRefreshContainer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.recyclerView.adapter = adapter
        binding.swipeRefreshContainer.setOnChildScrollUpCallback { parent, child ->
            binding.recyclerView.canScrollVertically(-1)
        }
        binding.swipeRefreshContainer.setOnRefreshListener {
            viewModel.refreshRepos()
        }
        handleUserInfo()
        handleRepos()
        binding.recyclerView.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                (recyclerView.layoutManager as? LinearLayoutManager)?.run {
                    val lastVisibleItem = this.findLastVisibleItemPosition()
                    if (!adapter.isRefreshing && lastVisibleItem == adapter.itemCount - 1 &&
                        !viewModel.isRepoLoading()
                    ) {
                        viewModel.loadMoreRepos()
                    }
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        handleNetworkState()
    }

    private fun handleNetworkState() {
        viewModel.networkState.observe(this) { isNetworkOnline ->
            isNetworkOnline ?: return@observe
            val viewGroup = findViewById<ViewGroup>(android.R.id.content)
            if (isNetworkOnline) {
                viewGroup.findViewById<TextView?>(R.id.no_network_title_view)?.run {
                    this.setBackgroundColor(Color.GREEN)
                    this.setText(R.string.internet_connected_title)
                    this.animate()
                        .setStartDelay(300L)
                        .translationY(this.measuredHeight.toFloat())
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                super.onAnimationEnd(animation)
                                viewGroup.findViewById<LinearLayout?>(R.id.no_network_view)
                                    ?.run {
                                        viewGroup.removeView(this)
                                    }
                            }
                        })
                }
            } else {
                var noInternetForm = viewGroup.findViewById<View?>(R.id.no_network_view)
                if (noInternetForm == null) {
                    noInternetForm = LayoutInflater.from(this)
                        .inflate(R.layout.base_no_network_view, viewGroup, false)
                    noInternetForm!!.setPadding(
                        0,
                        0,
                        0,
                        getNavigationBarHeight()
                    )
                    viewGroup.addView(noInternetForm)
                }
            }
        }
    }

    private fun getNavigationBarHeight(): Int {
        var navigationBarHeight = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = getSystemService(WindowManager::class.java).currentWindowMetrics
            val insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars())
            navigationBarHeight = insets.bottom
        } else {
            val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                navigationBarHeight = resources.getDimensionPixelSize(resourceId)
            }
        }
        return navigationBarHeight
    }

    private fun handleUserInfo() {
        viewModel.githubUserInfo.observe(this) { dataState ->
            when (dataState) {
                is DataState.Loading -> {
                    if (userFormLoading == null) {
                        userFormLoading = KunSkeleton.bind(binding.userForm)
                            .layout(R.layout.loading_github_user_form)
                            .run()
                    }
                }

                is DataState.Error -> {

                }

                is DataState.Success -> {
                    userFormLoading?.hide {
                        userFormLoading = null
                    }
                    val data = dataState.data
                    binding.userName.text = data.name
                    binding.userDescription.text = data.description
                    binding.userLocation.text = data.location
                    binding.userLink.text = data.blog
                    val followerNumStr = data.followers.format()
                    val followersStr = if (data.followers > 1) {
                        "followers"
                    } else {
                        "follower"
                    }
                    val span = SpannableString("$followerNumStr $followersStr")
                    span.setSpan(
                        ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)),
                        0,
                        followerNumStr.length,
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE
                    )
                    binding.follower.text = span
                }

                else -> {}
            }
        }
    }

    private fun handleRepos() {
        viewModel.githubRepos.observe(this) { dataState ->
            when (dataState) {
                is DataState.Loading -> {
                    if (reposLoading == null) {
                        reposLoading = KunSkeleton.bind(binding.recyclerView)
                            .adapter(adapter)
                            .layoutItem(R.layout.loading_item_github_repo)
                            .run()
                    }
                }

                is DataState.Error -> {
                    binding.swipeRefreshContainer.isRefreshing = false
                    AlertDialog.Builder(
                        this,
                        R.style.CustomAlertDialogTheme
                    )
                        .setMessage(dataState.throwable.message)
                        .setPositiveButton(R.string.reload_title) { dialog, which ->
                            viewModel.refreshRepos()
                        }
                        .setNegativeButton(android.R.string.cancel) { dialog, which ->
                            dialog.dismiss()
                        }
                        .show()
                }

                is DataState.Success -> {
                    binding.swipeRefreshContainer.isRefreshing = false
                    reposLoading?.hide {
                        reposLoading = null
                    }
                    adapter.onRefresh(dataState.data)
                }

                is DataState.LoadingMore -> {
                    adapter.onLoadingMore()
                    binding.recyclerView.scrollToPosition(adapter.itemCount)
                }

                is DataState.PaginationItem -> {
                    adapter.onAdd(dataState.data)
                }
            }
        }
    }

}
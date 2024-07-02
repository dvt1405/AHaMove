package com.kt.apps.media.ahamove

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kt.apps.media.ahamove.GithubRepoAdapter.ViewHolder.Companion.diffCallback
import com.kt.apps.media.core.models.GithubRepoDTO
import com.kt.apps.media.core.utils.format
import com.kt.apps.media.getBackgroundColor

class GithubRepoAdapter : ListAdapter<GithubRepoDTO, GithubRepoAdapter.BaseViewHolder>(diffCallback) {
    var itemClickListener: ((GithubRepoDTO, Int) -> Unit)? = null
    var isRefreshing = false
    private val _listItem by lazy {
        mutableListOf<GithubRepoDTO>()
    }

    override fun getItemViewType(position: Int): Int {
        return if (isRefreshing && position == _listItem.size) {
            R.layout.loading_item_github_repo
        } else {
            R.layout.item_github_repo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.loading_item_github_repo -> LoadingViewHolder(view)
            else -> ViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return if (isRefreshing) {
            _listItem.size + 1
        } else {
            _listItem.size
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val item = _listItem[position]
            holder.bindItem(item, itemClickListener)
        }
    }

    fun onRefresh(data: List<GithubRepoDTO>) {
        synchronized(_listItem) {
            _listItem.clear()
            _listItem.addAll(data)
            notifyDataSetChanged()
        }
    }

    fun onLoadingMore() {
        isRefreshing = true
        notifyItemInserted(_listItem.size)
    }

    fun onAdd(data: List<GithubRepoDTO>) {
        synchronized(_listItem) {
            _listItem.addAll(data)
        }
        isRefreshing = false
        notifyItemRangeChanged(_listItem.size, data.size)
    }

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class LoadingViewHolder(view: View) : BaseViewHolder(view)

    class ViewHolder(view: View) : BaseViewHolder(view) {
        private val repoNameTxt: TextView
        private val repoDescriptionTxt: TextView
        private val repoIsPrivateTxt: TextView
        private val repoLanguage: TextView
        private val repoStars: TextView
        private val repoForks: TextView

        init {
            repoNameTxt = view.findViewById(R.id.title)
            repoDescriptionTxt = view.findViewById(R.id.description)
            repoIsPrivateTxt = view.findViewById(R.id.is_private_tags)
            repoLanguage = view.findViewById(R.id.language)
            repoStars = view.findViewById(R.id.stars)
            repoForks = view.findViewById(R.id.forks)
        }

        fun bindItem(repoDTO: GithubRepoDTO, itemClickListener: ((GithubRepoDTO, Int) -> Unit)?) {
            repoNameTxt.text = repoDTO.name
            if (repoDTO.description.isNullOrEmpty()) {
                repoDescriptionTxt.visibility = View.GONE
            } else {
                repoDescriptionTxt.visibility = View.VISIBLE
                repoDescriptionTxt.text = repoDTO.description
            }
            repoIsPrivateTxt.text = if (repoDTO.private) {
                "private"
            } else {
                "public"
            }
            if (repoDTO.language.isNullOrEmpty()) {
                repoLanguage.visibility = View.GONE
            } else {
                repoLanguage.visibility = View.VISIBLE
                repoLanguage.text = repoDTO.language
            }
            repoDTO.language?.getBackgroundColor()?.let {
                TextViewCompat.setCompoundDrawableTintList(
                    repoLanguage,
                    ColorStateList.valueOf(ContextCompat.getColor(itemView.context, it))
                )
            }
            repoStars.text = repoDTO.stargazersCount.format()
            repoForks.text = repoDTO.forksCount.format()
            itemView.setOnClickListener {
                itemClickListener?.invoke(repoDTO, adapterPosition)
            }
        }

        companion object {
            val diffCallback = object : DiffUtil.ItemCallback<GithubRepoDTO>() {
                override fun areItemsTheSame(
                    oldItem: GithubRepoDTO,
                    newItem: GithubRepoDTO
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: GithubRepoDTO,
                    newItem: GithubRepoDTO
                ): Boolean {
                    return oldItem.toString() == newItem.toString()
                }
            }
        }
    }
}
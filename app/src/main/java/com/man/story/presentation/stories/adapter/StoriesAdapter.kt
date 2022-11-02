package com.man.story.presentation.stories.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.man.story.databinding.ItemStoryBinding
import com.man.story.domain.model.story.StoryModel

/**
 *
 * Created by Lukmanul Hakim on  03/10/22
 * devs.lukman@gmail.com
 */
class StoriesAdapter(
    private val onDetailItem: (ItemStoryBinding, StoryModel) -> Unit
) : PagingDataAdapter<StoryModel, StoriesAdapter.ViewHolder>(StoryModel.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, onDetailItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { data ->
            holder.bind(data)
        }
    }

    class ViewHolder(
        private val binding: ItemStoryBinding,
        private val onItemClick: (ItemStoryBinding, StoryModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: StoryModel) = with(binding) {
            itemView.setOnClickListener { onItemClick.invoke(binding, data) }

            Glide.with(itemView)
                .load(data.photoUrl)
                .into(ivItemPhoto)
            tvItemName.text = data.name
        }

    }


}
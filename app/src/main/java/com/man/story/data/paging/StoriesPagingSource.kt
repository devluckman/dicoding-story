package com.man.story.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.man.story.data.local.DataPreferences
import com.man.story.data.remote.Api
import com.man.story.data.response.stories.StoriesResponseDTO.Companion.toDomain
import com.man.story.data.response.stories.StoryItem
import com.man.story.domain.model.story.StoryModel
import kotlinx.coroutines.flow.first

/**
 *
 * Created by Lukmanul Hakim on  30/09/22
 * devs.lukman@gmail.com
 */
class StoriesPagingSource(
    private val api: Api,
    private val dataStore: DataPreferences
) : PagingSource<Int, StoryModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryModel> {
        return try {
            val token = dataStore.token.first()
            val bearerToken = "Bearer $token"

            val page = params.key ?: 1

            val response = api.getStories(
                bearerToken = bearerToken,
                page = page,
                size = params.loadSize
            )

            val dataList = response.body()?.listStory.toDomain()

            LoadResult.Page(
                data = dataList,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (dataList.isEmpty()) null else page + 1
            )
        } catch (t: Throwable) {
            LoadResult.Error(t)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, StoryModel>): Int? =
        state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
}
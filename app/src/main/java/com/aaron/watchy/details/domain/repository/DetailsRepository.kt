package com.aaron.watchy.details.domain.repository

import com.aaron.watchy.main.domain.model.Media
import com.aaron.watchy.util.Resource
import kotlinx.coroutines.flow.Flow

interface DetailsRepository {

    suspend fun getDetails(
        type: String,
        id: Int,
        isRefreshing: Boolean
    ): Flow<Resource<Media>>
}
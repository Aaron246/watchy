package com.aaron.watchy.details.data.repository

import android.app.Application
import com.aaron.watchy.R
import com.aaron.watchy.details.data.remote.api.DetailsApi
import com.aaron.watchy.details.data.remote.dto.DetailsDto
import com.aaron.watchy.details.domain.repository.DetailsRepository
import com.aaron.watchy.main.domain.model.Media
import com.aaron.watchy.main.domain.repository.MainRepository
import com.aaron.watchy.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DetailsRepositoryImpl @Inject constructor(
    private val detailsApi: DetailsApi,
    private val mainRepository: MainRepository,
    private val application: Application
): DetailsRepository {


    override suspend fun getDetails(
        type: String,
        id: Int,
        isRefreshing: Boolean
    ): Flow<Resource<Media>> {
        return flow {
            emit(Resource.Loading(true))

            val media = mainRepository.getMediaById(id)

            val isMediaExist = media.runtime != 0 || media.tagline.isNotEmpty()

            if (isMediaExist && !isRefreshing) {
                emit(Resource.Success(media))
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteDetailsDto = getRemoteDetails(type, id)

            remoteDetailsDto?.let { detailsDto ->
                val mediaWithDetails = media.copy(
                    runtime = detailsDto.runtime ?: 0,
                    tagline = detailsDto.tagline ?: ""
                )

                mainRepository.upsertMediaItem(mediaWithDetails)

                emit(Resource.Success(mainRepository.getMediaById(id)))
                emit(Resource.Loading(false))
                return@flow
            }

            emit(Resource.Error(application.getString(R.string.couldn_t_load_data)))
            emit(Resource.Loading(false))
        }
    }

    private suspend fun getRemoteDetails(
        type: String,
        id: Int
    ): DetailsDto? {
        val remoteDetailsDto = try {
            detailsApi.getDetails(type, id)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (e: HttpException) {
            e.printStackTrace()
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        return remoteDetailsDto
    }

}
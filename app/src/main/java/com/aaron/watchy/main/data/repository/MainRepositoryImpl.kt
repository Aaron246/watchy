package com.aaron.watchy.main.data.repository

import android.app.Application
import com.aaron.watchy.R
import com.aaron.watchy.main.data.local.MediaDatabase
import com.aaron.watchy.main.data.local.MediaEntity
import com.aaron.watchy.main.data.mappers.toMedia
import com.aaron.watchy.main.data.mappers.toMediaEntity
import com.aaron.watchy.main.data.remote.api.MediaApi
import com.aaron.watchy.main.domain.model.Media
import com.aaron.watchy.main.domain.repository.MainRepository
import com.aaron.watchy.util.APIConstants.MOVIE
import com.aaron.watchy.util.APIConstants.POPULAR
import com.aaron.watchy.util.APIConstants.TRENDING
import com.aaron.watchy.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val application: Application,
    private val mediaApi: MediaApi,
    mediaDatabase: MediaDatabase
): MainRepository {

    private val mediaDao = mediaDatabase.mediaDao

    override suspend fun upsertMediaList(mediaList: List<Media>) {
        mediaDao.upsertMediaList(mediaList.map { it.toMediaEntity() })
    }

    override suspend fun upsertMediaItem(media: Media) {
        mediaDao.upsertMediaItem(media.toMediaEntity())
    }

    override suspend fun getMediaById(id: Int): Media {
        return mediaDao.getMediaById(id).toMedia()
    }

    override suspend fun getMediaListByCategory(category: String): List<Media> {
        return mediaDao.getMediaListByCategory(category).map { it.toMedia() }
    }

    override suspend fun getTrending(
        forceFetchFromRemote: Boolean,
        isRefresh: Boolean,
        type: String,
        time: String,
        page: Int
    ): Flow<Resource<List<Media>>> {
        return flow {
            emit(Resource.Loading(true))

            val localMediaList = mediaDao.getMediaListByCategory(TRENDING)

            val loadJustFromCache =
                localMediaList.isNotEmpty() && !forceFetchFromRemote && !isRefresh

            if (loadJustFromCache) {
                emit(Resource.Success(localMediaList.map { it.toMedia() }))
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteMediaList = try {
                mediaApi.getTrending(
                    type, time, if (isRefresh) 1 else page
                )?.results
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(application.getString(R.string.couldn_t_load_data)))
                emit(Resource.Loading(false))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(application.getString(R.string.couldn_t_load_data)))
                emit(Resource.Loading(false))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(application.getString(R.string.couldn_t_load_data)))
                emit(Resource.Loading(false))
                return@flow
            }

            remoteMediaList?.let { mediaDtos ->  
                val entities = mediaDtos.map { mediaDto ->
                    mediaDto.toMediaEntity(
                        mediaDto.media_type ?: MOVIE,
                        TRENDING
                    )
                }

                if (isRefresh) {
                    mediaDao.deleteMediaListByCategory(TRENDING)
                }

                mediaDao.upsertMediaList(entities)

                emit(Resource.Success(entities.map { it.toMedia() }))
                emit(Resource.Loading(false))
                return@flow
            }

            emit(Resource.Error(application.getString(R.string.couldn_t_load_data)))
            emit(Resource.Loading(false))

        }
    }

    override suspend fun getMoviesAndTv(
        forceFetchFromRemote: Boolean,
        isRefresh: Boolean,
        type: String,
        category: String,
        page: Int
    ): Flow<Resource<List<Media>>> {
        return flow {
            emit(Resource.Loading(true))

            val localMediaList = mediaDao.getMediaListByTypeAndCategory(type, POPULAR)

            val loadJustFromCache =
                localMediaList.isNotEmpty() && !forceFetchFromRemote && !isRefresh

            if (loadJustFromCache) {
                emit(Resource.Success(localMediaList.map { it.toMedia() }))
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteMediaList = try {
                mediaApi.getMoviesAndTv(
                    type, POPULAR, if (isRefresh) 1 else page
                )?.results
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(application.getString(R.string.couldn_t_load_data)))
                emit(Resource.Loading(false))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(application.getString(R.string.couldn_t_load_data)))
                emit(Resource.Loading(false))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(application.getString(R.string.couldn_t_load_data)))
                emit(Resource.Loading(false))
                return@flow
            }

            remoteMediaList?.let { mediaDtos ->
                val entities = mediaDtos.map { mediaDto ->
                    mediaDto.toMediaEntity(type, POPULAR)
                }

                if (isRefresh) {
                    mediaDao.deleteMediaListByTypeAndCategory(type, POPULAR)
                }

                mediaDao.upsertMediaList(entities)

                emit(Resource.Success(entities.map { it.toMedia() }))
                emit(Resource.Loading(false))
                return@flow
            }

            emit(Resource.Error(application.getString(R.string.couldn_t_load_data)))
            emit(Resource.Loading(false))

        }
    }
}
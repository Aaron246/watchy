package com.aaron.watchy.details.data.remote.api

import com.aaron.watchy.details.data.remote.dto.DetailsDto
import com.aaron.watchy.details.data.remote.dto.VideosDto
import com.aaron.watchy.main.data.remote.api.MediaApi
import com.aaron.watchy.main.data.remote.dto.MediaListDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DetailsApi {

    @GET("{type}/{id}")
    suspend fun getDetails(
        @Path("type") type: String,
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = MediaApi.API_KEY
    ): DetailsDto?

    @GET("{type}/{id}/videos")
    suspend fun getVideos(
        @Path("type") type: String,
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = MediaApi.API_KEY
    ): VideosDto?

    @GET("{type}/{id}/similar")
    suspend fun getSimilar(
        @Path("type") type: String,
        @Path("id") id: Int,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = MediaApi.API_KEY
    ): MediaListDto?

}
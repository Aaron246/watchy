package com.aaron.watchy.main.presentation

import com.aaron.watchy.main.domain.model.Media

data class MainState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,

    val trendingPage: Int = 1,
    val tvPage: Int = 1,
    val moviesPage: Int = 1,

    val trendingList: List<Media> = emptyList(),
    val tvList: List<Media> = emptyList(),
    val moviesList: List<Media> = emptyList(),

    // 2 from each 3 above.
    val specialList: List<Media> = emptyList(),

    val name: String = "Ahmed"
)

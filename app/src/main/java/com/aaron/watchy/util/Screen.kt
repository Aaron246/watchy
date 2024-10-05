package com.aaron.watchy.util

sealed class Screen(val route: String) {

    object Main: Screen("main")
    object Trending: Screen("trending")
    object Tv: Screen("tv")
    object Movies: Screen("movies")
}
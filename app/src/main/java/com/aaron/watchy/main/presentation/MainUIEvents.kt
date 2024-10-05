package com.aaron.watchy.main.presentation

sealed class MainUIEvents {

    data class Refresh(val route: String): MainUIEvents()

    data class Paginate(val route: String): MainUIEvents()
}
package com.aaron.watchy.main.domain.usecase

import com.aaron.watchy.util.APIConstants.genres

object GenreIdsToString {
    fun genreIdsToString(genreIds: List<String>) : String {
        return genreIds.map { id ->
            genres.find { genre ->
                genre.genreId.toString() == id
            }?.genreName
        }.joinToString(" - ")

    }
}
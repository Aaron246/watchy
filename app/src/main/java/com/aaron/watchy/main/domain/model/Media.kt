package com.aaron.watchy.main.domain.model

data class Media(
    val mediaId: Int,

    val adult: Boolean,
    val backdropPath: String,
    val genreIds: List<String>,
    val mediaType: String,
    val originCountry: List<String>,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val title: String,
    val voteAverage: Double,
    val voteCount: Int,
    var category: String,

    val runtime: Int,
    val tagline: String,

    val videosIds: List<String>,
    val similarMediaIds: List<Int>
)

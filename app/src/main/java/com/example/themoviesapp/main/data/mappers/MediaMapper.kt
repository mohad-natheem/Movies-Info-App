package com.example.themoviesapp.main.data.mappers

import com.example.themoviesapp.main.data.local.media.MediaEntity
import com.example.themoviesapp.main.data.remote.dto.MediaDto
import com.example.themoviesapp.main.domain.model.Media
import com.example.themoviesapp.util.Constants

fun MediaEntity.toMedia(
    type: String,
    category: String
): Media {
    return Media(
        backdropPath = backdropPath ?: Constants.UNAVAILABLE,
        originalLanguage = originalLanguage ?: Constants.UNAVAILABLE,
        overview = overview ?: Constants.UNAVAILABLE,
        posterPath = posterPath ?: Constants.UNAVAILABLE,
        releaseDate = releaseDate ?: firstAirDate ?: Constants.UNAVAILABLE,
        title = title ?: Constants.UNAVAILABLE,
        voteAverage = voteAverage ?: 0.0,
        popularity = popularity ?: 0.0,
        voteCount = voteCount ?: 0,
        genreIds = try {
            genreIds?.split(",")!!.map { it.toInt() }
        } catch (e: Exception) {
            listOf(-1, -2)
        },
        id = id ?: 1,
        adult = adult ?: false,
        mediaType = type,
        originCountry = try {
            originCountry?.split(",")!!.map { it }
        } catch (e: Exception) {
            listOf("-1", "-2")
        },
        originalTitle = originalTitle ?: originalName ?: Constants.UNAVAILABLE,
        category = category,
        runtime = runtime ?: 0,
        status = status ?: "",
        tagline = tagline ?: "",
        videos = try {
            videos?.split(",")?.map { it }
        } catch (e: Exception) {
            listOf("-1", "-2")
        },
        similarMediaList = try {
            similarMediaList?.split(",")!!.map { it.toInt() }
        } catch (e: Exception) {
            listOf(-1, -2)
        },
    )
}

fun MediaDto.toMediaEntity(
    type: String,
    category: String,
): MediaEntity {
    return MediaEntity(
        backdropPath = backdrop_path ?: Constants.UNAVAILABLE,
        originalLanguage = original_language ?: Constants.UNAVAILABLE,
        overview = overview ?: Constants.UNAVAILABLE,
        posterPath = poster_path ?: Constants.UNAVAILABLE,
        releaseDate = release_date ?: "-1,-2",
        title = title ?: name ?: Constants.UNAVAILABLE,
        originalName = original_name ?: Constants.UNAVAILABLE,
        voteAverage = vote_average ?: 0.0,
        popularity = popularity ?: 0.0,
        voteCount = vote_count ?: 0,
        genreIds = try {
            genre_ids?.joinToString(",") ?: "-1,-2"
        } catch (e: Exception) {
            "-1,-2"
        },
        id = id ?: 1,
        adult = adult ?: false,
        mediaType = type,
        category = category,
        originCountry = try {
            origin_country?.joinToString(",") ?: "-1,-2"
        } catch (e: Exception) {
            "-1,-2"
        },
        originalTitle = original_title ?: original_name ?: Constants.UNAVAILABLE,
        videos = try {
            videos?.joinToString(",") ?: "-1,-2"
        } catch (e: Exception) {
            "-1,-2"
        },
        similarMediaList = try {
            similarMediaList?.joinToString(",") ?: "-1,-2"
        } catch (e: Exception) {
            "-1,-2"
        },
        firstAirDate = first_air_date ?: "",
        video = video ?: false,

        status = "",
        runtime = 0,
        tagline = "",
    )
}


fun MediaDto.toMedia(
    type: String,
    category: String,
): Media {
    return Media(
        backdropPath = backdrop_path ?: Constants.UNAVAILABLE,
        originalLanguage = original_language ?: Constants.UNAVAILABLE,
        overview = overview ?: Constants.UNAVAILABLE,
        posterPath = poster_path ?: Constants.UNAVAILABLE,
        releaseDate = release_date ?: Constants.UNAVAILABLE,
        title = title ?: name ?: Constants.UNAVAILABLE,
        voteAverage = vote_average ?: 0.0,
        popularity = popularity ?: 0.0,
        voteCount = vote_count ?: 0,
        genreIds = genre_ids ?: emptyList(),
        id = id ?: 1,
        adult = adult ?: false,
        mediaType = type,
        category = category,
        originCountry = origin_country ?: emptyList(),
        originalTitle = original_title ?: original_name ?: Constants.UNAVAILABLE,
        runtime = null,
        status = null,
        tagline = null,
        videos = videos,
        similarMediaList = similarMediaList ?: emptyList()
    )
}

fun Media.toMediaEntity(): MediaEntity {
    return MediaEntity(
        backdropPath = backdropPath,
        originalLanguage = originalLanguage,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        voteAverage = voteAverage,
        popularity = popularity,
        voteCount = voteCount,
        genreIds = try {
            genreIds.joinToString(",")
        } catch (e: Exception) {
            "-1,-2"
        },
        id = id,
        adult = adult,
        mediaType = mediaType,
        originCountry = try {
            originCountry.joinToString(",")
        } catch (e: Exception) {
            "-1,-2"
        },
        originalTitle = originalTitle,
        category = category,
        videos = try {
            videos?.joinToString(",") ?: "-1,-2"
        } catch (e: Exception) {
            "-1,-2"
        },
        similarMediaList = try {
            similarMediaList.joinToString(",")
        } catch (e: Exception) {
            "-1,-2"
        },
        video = false,
        firstAirDate = releaseDate,
        originalName = originalTitle,

        status = status ?: "",
        runtime = runtime ?: 0,
        tagline = tagline ?: ""
    )
}

package com.example.themoviesapp.main.data.repository

import com.example.themoviesapp.main.data.local.genres.GenreDatabase
import com.example.themoviesapp.main.data.local.genres.GenreEntity
import com.example.themoviesapp.main.data.remote.api.GenreApi
import com.example.themoviesapp.main.domain.model.Genre
import com.example.themoviesapp.main.domain.repository.GenreRepository
import com.example.themoviesapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GenreRepositoryImpl @Inject constructor(
    private val genreApi: GenreApi,
    private val genreDatabase: GenreDatabase
): GenreRepository {

    private val genreDao = genreDatabase.genreDao

    override suspend fun getGenres(
        fetchFromRemote: Boolean,
        type: String,
        apiKey: String
    ): Flow<Resource<List<Genre>>> {
        return flow{
            emit(Resource.Loading(isLoading = true))

            val localGenreEntity = genreDao.getGenres(
                mediaType = type
            )
            val shouldLoadFromCache = localGenreEntity.isNotEmpty() && !fetchFromRemote
            if(shouldLoadFromCache){
                emit(Resource.Success(
                    data = localGenreEntity.map {genre ->
                        Genre(
                            id = genre.id,
                            name = genre.name,
                        )

                    }
                ))
                emit(Resource.Loading(isLoading = false))
                return@flow
            }
            val remoteGenreList = try{
                genreApi.getGenresList(
                    type = type,
                    apiKey = apiKey
                ).genres
            }catch (e: IOException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                emit(Resource.Loading(isLoading = false))
                return@flow

            }catch (e: HttpException){
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
                emit(Resource.Loading(isLoading = false))
                return@flow
            }
            genreDao.insertGenres(
                genresList = remoteGenreList.map {genre ->
                    GenreEntity(
                        id = genre.id,
                        name = genre.name,
                        type = type
                    )
                }
            )
//            val genreList = genreDao.getGenres(
//                mediaType = type
//            ).map {genre ->
//                Genre(
//                    id = genre.id,
//                    name = genre.name
//                )
//            }
            emit(Resource.Success(
                data = remoteGenreList
            ))
            emit(Resource.Loading(isLoading = false))
        }
    }
}